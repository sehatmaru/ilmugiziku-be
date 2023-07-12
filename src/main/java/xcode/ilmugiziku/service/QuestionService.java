package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.model.TemplateQuestionRelModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.repository.TemplateQuestionRepository;
import xcode.ilmugiziku.domain.request.exam.ExamResultRequest;
import xcode.ilmugiziku.domain.request.question.CreateUpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.CreateUpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.answer.AnswerResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.AnswerMapper;
import xcode.ilmugiziku.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class QuestionService {

   @Autowired private QuestionRepository questionRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private TemplateQuestionRepository templateQuestionRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   public BaseResponse<List<QuestionResponse>> getQuestionList() {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();

      try {
         List<QuestionResponse> result = questionMapper.modelToResponses(questionRepository.findAll());
         result.forEach(e-> {
            List<AnswerModel> models = answerRepository.getAnswersByQuestion(e.getSecureId());
            List<AnswerResponse> responses = answerMapper.modelToResponses(models);

            e.setAnswers(responses);
         });

         response.setSuccess(result);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> createQuestion(List<CreateUpdateQuestionRequest> request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         request.forEach(e-> {
            if (!e.isValid()) throw new AppException(ANSWER_LENGTH_ERROR_MESSAGE);
            if (!e.isOneCorrectAnswer()) throw new AppException(MULTIPLE_CORRECT_ANSWER_ERROR_MESSAGE);

            QuestionModel model = questionMapper.createRequestToModel(e);
            List<AnswerModel> answers = answerMapper.createRequestToModels(e.getAnswers(), model.getSecureId());

            questionRepository.save(model);
            answerRepository.saveAll(answers);
         });

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateQuestion(String questionSecureId, CreateUpdateQuestionRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      QuestionModel model = questionRepository.findBySecureId(questionSecureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (!request.isValid()) throw new AppException(ANSWER_LENGTH_ERROR_MESSAGE);
      if (!request.isOneCorrectAnswer()) throw new AppException(MULTIPLE_CORRECT_ANSWER_ERROR_MESSAGE);

      try {
         List<AnswerModel> answers = answerRepository.getAnswersByQuestion(questionSecureId);

         for (int i = 0; i < answers.size(); i++) {
            AnswerModel ans =  answers.get(i);
            CreateUpdateAnswerRequest req =  request.getAnswers().get(i);

            ans.setContent(req.getContent());
            ans.setCorrectAnswer(req.isCorrectAnswer());
         }

         questionRepository.save(questionMapper.updateRequestToModel(model, request));
         answerRepository.saveAll(answers);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteQuestion(String questionSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      QuestionModel model = questionRepository.findBySecureId(questionSecureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         List<AnswerModel> answers = answerRepository.getAnswersByQuestion(questionSecureId);

         answerRepository.deleteAll(answers);
         questionRepository.delete(model);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public List<QuestionResponse> getQuestionsByTemplate(String template) {
      List<TemplateQuestionRelModel> templateQuestion = templateQuestionRepository.getTemplateQuestionByTemplate(template);
      List<QuestionModel> questions = new ArrayList<>();

      templateQuestion.forEach(e-> questions.add(questionRepository.findBySecureId(e.getQuestion())));

      List<QuestionResponse> result = questionMapper.modelToResponses(questions);
      result.forEach(e-> {
         List<AnswerModel> models = answerRepository.getAnswersByQuestion(e.getSecureId());
         List<AnswerResponse> responses = answerMapper.modelToResponses(models);

         e.setAnswers(responses);
      });

      return result;
   }

   public ExamResultResponse calculateScore(List<ExamResultRequest> request) {
      ExamResultResponse result = new ExamResultResponse();

      int correct = 0;
      int incorrect = 0;
      int blank = 0;

      for (ExamResultRequest exam : request) {
         if (exam.getAnswer().isEmpty()) blank += 1;
         else {
            if (answerRepository.getCorrectAnswer(exam.getQuestion()).getSecureId().equals(exam.getAnswer())) correct += 1;
            else incorrect += 1;
         }
      }

      result.setScore((correct / (request.size() * 1.0)) * 100);
      result.setBlank(blank);
      result.setCorrect(correct);
      result.setIncorrect(incorrect);

      return result;
   }
}
