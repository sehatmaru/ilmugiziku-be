package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.model.AnswerModel;
import xcode.marsiajar.domain.model.QuestionModel;
import xcode.marsiajar.domain.model.TemplateQuestionRelModel;
import xcode.marsiajar.domain.repository.AnswerRepository;
import xcode.marsiajar.domain.repository.QuestionRepository;
import xcode.marsiajar.domain.repository.TemplateQuestionRepository;
import xcode.marsiajar.domain.request.exam.ExamResultRequest;
import xcode.marsiajar.domain.request.question.CreateUpdateAnswerRequest;
import xcode.marsiajar.domain.request.question.CreateUpdateQuestionRequest;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.answer.AnswerResponse;
import xcode.marsiajar.domain.response.exam.ExamResultResponse;
import xcode.marsiajar.domain.response.question.QuestionListResponse;
import xcode.marsiajar.domain.response.question.QuestionResponse;
import xcode.marsiajar.exception.AppException;
import xcode.marsiajar.mapper.AnswerMapper;
import xcode.marsiajar.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static xcode.marsiajar.shared.ResponseCode.*;

@Service
public class QuestionService {

   @Autowired private CategoryService categoryService;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private TemplateQuestionRepository templateQuestionRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   /**
    * get all question list
    * @return question list
    */
   public BaseResponse<List<QuestionListResponse>> getQuestionList(String content, String categorySecureId) {
      BaseResponse<List<QuestionListResponse>> response = new BaseResponse<>();

      try {
         List<QuestionListResponse> result = questionMapper.modelToListResponses(questionRepository.findAllQuestions());
         result.forEach(e -> e.setCategory(categoryService.getCategoryName(e.getCategorySecureId())));

         result = result.stream()
                 .filter(e -> e.getContent().toLowerCase().contains(content.toLowerCase()))
                 .collect(Collectors.toList());

         if (!categorySecureId.isEmpty()) {
            result = result.stream()
                    .filter(e -> e.getCategory().equals(categorySecureId))
                    .collect(Collectors.toList());
         }

         response.setSuccess(result);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * create a question,
    * it must have 5 answer
    * and 1 correct answer
    * @param request body
    * @return boolean
    */
   public BaseResponse<Boolean> createQuestion(List<CreateUpdateQuestionRequest> request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         request.forEach(e-> {
            if (!e.isValid()) throw new AppException(ANSWER_LENGTH_ERROR_MESSAGE);
            if (!e.isOneCorrectAnswer()) throw new AppException(MULTIPLE_CORRECT_ANSWER_ERROR_MESSAGE);

            QuestionModel model = questionMapper.createRequestToModel(e);
            model.setCategory(categoryService.getCategoryName(model.getCategory()));
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

   /**
    * update the question,
    * it will delete the existing
    * question-answer relation on t_answer
    * and insert the new answers relation
    * @param questionSecureId string
    * @param request body
    * @return boolean
    */
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

   /**
    * delete question,
    * include delete answer on t_answer
    * @param questionSecureId string
    * @return boolean
    */
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

   /**
    * detail question with answers
    * @param questionSecureId string
    * @return detail
    */
   public BaseResponse<QuestionResponse> getQuestionDetail(String questionSecureId) {
      BaseResponse<QuestionResponse> response = new BaseResponse<>();

      QuestionModel question = questionRepository.findBySecureId(questionSecureId);

      if (question == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         QuestionResponse result = questionMapper.modelToResponse(question);
         result.setCategory(categoryService.getCategoryName(result.getCategorySecureId()));
         List<AnswerModel> models = answerRepository.getAnswersByQuestion(questionSecureId);
         List<AnswerResponse> responses = answerMapper.modelToResponses(models);

         result.setAnswers(responses);

         response.setSuccess(result);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * get all question based on template
    * @param template = template secure id
    * @return question list
    */
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

   /**
    * calculate score based on
    * submitted exam
    * @param request body
    * @return result
    */
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
