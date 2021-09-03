package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.question.QuestionExamResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;
import xcode.ilmugiziku.mapper.AnswerMapper;
import xcode.ilmugiziku.mapper.QuestionMapper;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class QuestionService implements QuestionPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final AnswerService answerService;
   private final ScheduleService scheduleService;

   private final QuestionRepository questionRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   public QuestionService(AuthTokenService authTokenService, AnswerService answerService, ScheduleService scheduleService, QuestionRepository questionRepository, AuthService authService) {
      this.authTokenService = authTokenService;
      this.questionRepository = questionRepository;
      this.answerService = answerService;
      this.scheduleService = scheduleService;
      this.authService = authService;
   }

   @Override
   public BaseResponse<List<QuestionAnswerResponse>> getQuizQuestions(String token) {
      return getQuiz(token, QUIZ);
   }

   @Override
   public BaseResponse<List<QuestionAnswerResponse>> getPracticeQuestions(String token) {
      return getQuiz(token, PRACTICE);
   }

   @Override
   public BaseResponse<QuestionResponse> getTryOutQuestion(String token, int questionType, int questionSubType) {
      BaseResponse<QuestionResponse> response = new BaseResponse<>();
      QuestionResponse questionResponse = new QuestionResponse();

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
         if (questionSubType > 0 && questionSubType < 5) {
            if (authTokenService.isValidToken(token)) {
               AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

               if (authService.isRoleAdmin(authTokenModel.getAuthSecureId())) {
                  response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, ADMIN));
               } else {
                  ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());

                  if (scheduleModel.getSecureId() != null) {
                     questionResponse.setTimeLimit(scheduleModel.getTimeLimit());

                     response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, CONSUMER));
                  } else {
                     response.setWrongParams();
                  }
               }
            } else {
               response.setFailed(TOKEN_ERROR_MESSAGE);
            }
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
         QuestionModel model = questionMapper.createRequestToModel(request);

         try {
            questionRepository.save(model);

            for (CreateAnswerRequest answer : request.getAnswers()) {
               createAnswer(answer, model.getSecureId());
            }

            createResponse.setSecureId(model.getSecureId());

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
            try {
               AnswerModel model = answerService.getAnswerBySecureId(answer.getSecureId());

               answerService.save(answerMapper.updateRequestToModel(model, answer));
            } catch (Exception e) {
               response.setFailed(e.toString());
            }
         }

         try {
            QuestionModel model = questionRepository.findBySecureId(request.getSecureId());

            questionRepository.save(questionMapper.updateRequestToModel(model, request));

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
      try {
         answerService.save(answerMapper.createRequestToModel(request, questionSecureId));
      } catch (Exception e){
         System.out.println(e.getMessage());
      }
   }

   private QuestionResponse getTryOut(QuestionResponse questionResponse, int questionType, int questionSubType, int role) {
      List<QuestionModel> questionModels = new ArrayList<>();
      List<AnswerModel> answerModels = new ArrayList<>();

      try {
         if (questionSubType == 0) {
            questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);
         } else {
            questionModels = questionRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);
         }
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }

      if (questionModels != null) {
         List<QuestionExamResponse> questionExamResponses = new ArrayList<>();

         for (QuestionModel question : questionModels) {
            QuestionExamResponse questionExamResponse = questionMapper.modelToQuestionExamResponse(question, role);

            try {
               answerModels = answerService.getAnswerListByQuestionSecureId(question.getSecureId());
            } catch (Exception e) {
               System.out.println(e.getMessage());
            }

            questionExamResponse.setAnswers(answerMapper.modelsToAnswerResponses(answerModels));

            questionExamResponses.add(questionExamResponse);
         }

         questionResponse.setExam(questionExamResponses);
      }

      return questionResponse;
   }

   private BaseResponse<List<QuestionAnswerResponse>> getQuiz(String token, int questionType) {
      BaseResponse<List<QuestionAnswerResponse>> response = new BaseResponse<>();
      List<QuestionAnswerResponse> questionResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         List<AnswerModel> answerModels = new ArrayList<>();

         List<QuestionModel> questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);

         if (questionModels != null) {
            for (QuestionModel question : questionModels) {
               QuestionAnswerResponse questionResponse = questionMapper.modelToQuestionValueResponse(question);

               try {
                  answerModels = answerService.getAnswerListByQuestionSecureId(question.getSecureId());
               } catch (Exception e) {
                  response.setFailed(e.toString());
               }

               questionResponse.setAnswers(answerMapper.modelsToAnswerValueResponses(answerModels));

               questionResponses.add(questionResponse);
            }

            response.setSuccess(questionResponses);
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public QuestionModel getQuestionBySecureId(String secureId) {
      return questionRepository.findBySecureId(secureId);
   }
}
