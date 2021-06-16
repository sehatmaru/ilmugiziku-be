package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.presenter.AdminPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.PRACTICE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

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

      if (validate(request)) {
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

   private boolean validate(CreateQuestionRequest request) {
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

}
