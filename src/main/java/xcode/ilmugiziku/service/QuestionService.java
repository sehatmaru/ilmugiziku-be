package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.response.AnswerResponse;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.ArrayList;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.*;

@Service
public class QuestionService implements QuestionPresenter {

   final QuestionRepository questionRepository;
   final AnswerRepository answerRepository;

   public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
      this.questionRepository = questionRepository;
      this.answerRepository = answerRepository;
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getQuizQuestions() {
      return getQuestions(QUIZ, NONE);
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getPracticeQuestions() {
      return getQuestions(PRACTICE, NONE);
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getTryOutQuestion(int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
         if (questionSubType > 0 && questionSubType < 5) {
            response = getQuestions(questionType, questionSubType);
         } else {
            response.setStatusCode(PARAMS_CODE);
            response.setMessage(PARAMS_ERROR_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   private BaseResponse<List<QuestionResponse>> getQuestions(int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();
      List<QuestionResponse> questionResponses = new ArrayList<>();

      List<QuestionModel> models = new ArrayList<>();

      try {
         if (questionSubType == 0) {
            models = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);
         } else {
            models = questionRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);
         }
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
            questionResponse.setQuestionSubType(questionResponse.getQuestionSubType());

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
