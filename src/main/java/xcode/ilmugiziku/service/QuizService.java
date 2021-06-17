package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.presenter.QuizPresenter;

import java.util.ArrayList;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

@Service
public class QuizService implements QuizPresenter {

   @Autowired
   QuestionRepository questionRepository;

   @Autowired
   AnswerRepository answerRepository;

   @Override
   public BaseResponse<List<QuestionResponse>> getQuizQuestions() {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();
      List<QuestionResponse> questionResponses = new ArrayList<>();

      List<QuestionModel> models = new ArrayList<>();

      try {
          models = questionRepository.findByQuestionTypeAndDeletedAtIsNull(QUIZ);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(e.toString());
      }

      if (models != null) {
         for (QuestionModel question : models) {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setSecureId(question.getSecureId());
            questionResponse.setContent(question.getContent());
            questionResponse.setQuestionType(question.getQuestionType());

            List<AnswerModel> answerModels = new ArrayList<>();

            try {
               answerModels = answerRepository.findAllByQuestionSecureId(question.getSecureId());
            } catch (Exception e) {
               response.setStatusCode(FAILED_CODE);
               response.setMessage(e.toString());
            }

            List<AnswerResponse> answers = new ArrayList<>();
            for (AnswerModel answer : answerModels) {
               AnswerResponse answerResponse = new AnswerResponse();
               answerResponse.setSecureId(answer.getSecureId());
               answerResponse.setContent(answer.getContent());
               answerResponse.setValue(answer.isValue());

               answers.add(answerResponse);

               questionResponse.setAnswers(answers);
            }

            questionResponses.add(questionResponse);
         }

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);
         response.setResult(questionResponses);
      }

      return response;
   }

}
