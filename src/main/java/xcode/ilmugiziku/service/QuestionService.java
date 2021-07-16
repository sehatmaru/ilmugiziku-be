package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.AnswerResponse;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.NONE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@Service
public class QuestionService implements QuestionPresenter {

   private final AuthTokenService authTokenService;
   private final AnswerService answerService;

   private final QuestionRepository questionRepository;

   public QuestionService(AuthTokenService authTokenService, AnswerService answerService, QuestionRepository questionRepository) {
      this.authTokenService = authTokenService;
      this.questionRepository = questionRepository;
      this.answerService = answerService;
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getQuizQuestions(String token) {
      return getQuestions(token, QUIZ, NONE);
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getPracticeQuestions(String token) {
      return getQuestions(token, PRACTICE, NONE);
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getTryOutQuestion(String token, int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
         if (questionSubType > 0 && questionSubType < 5) {
            response = getQuestions(token, questionType, questionSubType);
         } else {
            response.setWrongParams();
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createQuestion(String token, CreateQuestionRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (request.validate()) {
         String questionTempSecureId = generateSecureId();

         for (CreateAnswerRequest answer : request.getAnswers()) {
            createAnswer(answer, questionTempSecureId);
         }

         QuestionModel model = new QuestionModel();
         model.setSecureId(questionTempSecureId);
         model.setContent(request.getContent());
         model.setQuestionType(request.getQuestionType());
         model.setQuestionSubType(request.getQuestionSubType());
         model.setCreatedAt(new Date());

         try {
            questionRepository.save(model);

            createResponse.setSecureId(questionTempSecureId);

            response.setSuccess(createResponse);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateQuestion(String token, UpdateQuestionRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (request.validate()) {
         for (UpdateAnswerRequest answer : request.getAnswers()) {
            AnswerModel model = new AnswerModel();

            try {
               model = answerService.getAnswerBySecureId(answer.getSecureId());
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            model.setContent(answer.getContent());
            model.setValue(answer.isValue());
            model.setUpdatedAt(new Date());

            try {
               answerService.saveByModel(model);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         }

         QuestionModel model = new QuestionModel();

         try {
            model = questionRepository.findBySecureId(request.getSecureId());
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         model.setContent(request.getContent());
         model.setQuestionType(request.getQuestionType());
         model.setQuestionSubType(request.getQuestionSubType());
         model.setUpdatedAt(new Date());

         try {
            questionRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteQuestion(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      QuestionModel model = new QuestionModel();

      try {
         model = questionRepository.findBySecureId(secureId);
      } catch (Exception e) {
         response.setFailed(e.toString());
      }

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            questionRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setNotFound("");
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
         answerService.saveByModel(model);
      } catch (Exception e){
         System.out.println(e.getMessage());
      }
   }

   private BaseResponse<List<QuestionResponse>> getQuestions(String token, int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();
      List<QuestionResponse> questionResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         List<QuestionModel> models = new ArrayList<>();

         try {
            if (questionSubType == 0) {
               models = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);
            } else {
               models = questionRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);
            }
         } catch (Exception e) {
            response.setFailed(e.toString());
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
                  answerModels = answerService.getAnswerListByQuestionSecureId(question.getSecureId());
               } catch (Exception e) {
                  response.setFailed(e.toString());
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

            response.setSuccess(questionResponses);
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }
}
