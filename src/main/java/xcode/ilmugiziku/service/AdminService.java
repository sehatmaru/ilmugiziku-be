package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.presenter.AdminPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@Service
public class AdminService implements AdminPresenter {

   @Autowired
   QuestionRepository questionRepository;

   @Autowired
   AnswerRepository answerRepository;

   @Override
   public BaseResponse<CreateBaseResponse> createQuestion(CreateQuestionRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (validateCreateRequest(request)) {
         String questionTempSecureId = generateSecureId();

         for (CreateAnswerRequest answer : request.getAnswers()) {
            createAnswer(answer, questionTempSecureId);
         }

         QuestionModel model = new QuestionModel();
         model.setSecureId(questionTempSecureId);
         model.setContent(request.getContent());
         model.setQuestionType(request.getQuestionType());
         model.setCreatedAt(new Date());

         try {
            questionRepository.save(model);

            response.setStatusCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);
            createResponse.setSecureId(questionTempSecureId);

            response.setResult(createResponse);
         } catch (Exception e){
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateQuestion(UpdateQuestionRequest request, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (validateUpdateRequest(request, secureId)) {
         for (UpdateAnswerRequest answer : request.getAnswers()) {
            AnswerModel model = new AnswerModel();

            try {
               model = answerRepository.findBySecureId(answer.getSecureId());
            } catch (Exception e) {
               response.setStatusCode(FAILED_CODE);
               response.setMessage(FAILED_MESSAGE);
            }

            model.setContent(answer.getContent());
            model.setValue(answer.isValue());
            model.setUpdatedAt(new Date());

            try {
               answerRepository.save(model);
            } catch (Exception e){
               response.setStatusCode(FAILED_CODE);
               response.setMessage(FAILED_MESSAGE);
            }
         }

         QuestionModel model = new QuestionModel();

         try {
            model = questionRepository.findBySecureId(secureId);
         } catch (Exception e) {
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }

         model.setContent(request.getContent());
         model.setQuestionType(request.getQuestionType());
         model.setUpdatedAt(new Date());

         try {
            questionRepository.save(model);

            response.setStatusCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);

            response.setResult(true);
         } catch (Exception e){
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteQuestion(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      QuestionModel model = new QuestionModel();

      try {
         model = questionRepository.findBySecureId(secureId);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(FAILED_MESSAGE);
      }

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            questionRepository.save(model);

            response.setStatusCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);

            response.setResult(true);
         } catch (Exception e){
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }
      } else {
         response.setStatusCode(NOT_FOUND_CODE);
         response.setMessage(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   private void createAnswer(CreateAnswerRequest request, String questionSecureId) {
      AnswerModel model = new AnswerModel();
      model.setSecureId(generateSecureId());
      model.setContent(request.getContent());
      model.setQuestionSecureId(questionSecureId);
      model.setValue(request.isValue());
      model.setCreatedAt(new Date());

      try {
         answerRepository.save(model);
      } catch (Exception e){
         System.out.println(e);
      }
   }

   private boolean validateCreateRequest(CreateQuestionRequest request) {
      boolean result = true;

      if (request.getAnswers().size() != 5) {
         result = false;
      } else {
         int count = 0;

         for (CreateAnswerRequest answer : request.getAnswers()) {
            if (answer.isValue()) {
               count+=1;
            }
         }

         if (count > 1) {
            result = false;
         }
      }

      if (request.getQuestionType() != QUIZ && request.getQuestionType() != PRACTICE) {
         result = false;
      }

      return result;
   }

   private boolean validateUpdateRequest(UpdateQuestionRequest request, String secureId) {
      boolean result = true;

      if (request.getAnswers().size() != 5) {
         result = false;
      } else {
         int count = 0;

         for (UpdateAnswerRequest answer : request.getAnswers()) {
            if (answer.isValue()) {
               count+=1;
            }

            if (answer.getSecureId() == null) {
               result = false;
            }
         }

         if (count > 1) {
            result = false;
         }
      }

      if (request.getQuestionType() != QUIZ
              && request.getQuestionType() != PRACTICE
              && request.getQuestionType() != TRY_OUT_UKOM
              && request.getQuestionType() != TRY_OUT_SKB_GIZI) {
         result = false;
      }

      if (secureId == null) {
         result = false;
      } else {
         try {
            if (questionRepository.findBySecureId(secureId) == null) {
               result = false;
            } else {
               for (UpdateAnswerRequest answer : request.getAnswers()) {
                  if (answerRepository.findBySecureId(answer.getSecureId()) == null) {
                     result = false;
                  }
               }
            }
         } catch (Exception e) {
            System.out.println(e);
         }
      }

      return result;
   }

}
