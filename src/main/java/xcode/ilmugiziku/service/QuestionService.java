package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;
import xcode.ilmugiziku.domain.response.question.QuestionExamResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.AnswerMapper;
import xcode.ilmugiziku.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;
import static xcode.ilmugiziku.shared.refs.TimeLimitRefs.TIME_LIMIT_SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TimeLimitRefs.TIME_LIMIT_UKOM;

@Service
public class QuestionService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthService authService;
   @Autowired private TemplateService templateService;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private TemplateRepository templateRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   public BaseResponse<List<QuestionAnswerResponse>> getQuizQuestions(String token) {
      return getQuiz(token, QUIZ);
   }

   public BaseResponse<List<QuestionAnswerResponse>> getPracticeQuestions(String token) {
      return getQuiz(token, PRACTICE);
   }

   public BaseResponse<QuestionResponse> getTryOutQuestion(String token, int questionType, int questionSubType, String templateSecureId) {
      BaseResponse<QuestionResponse> response;

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
         if (questionSubType > 0 && questionSubType < 5) {
            response = getQuestions(token, questionType, questionSubType, templateSecureId);
         } else {
            throw new AppException(PARAMS_ERROR_MESSAGE);
         }
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   private BaseResponse<QuestionResponse> getQuestions(String token, int questionType, int questionSubType, String templateSecureId) {
      BaseResponse<QuestionResponse> response = new BaseResponse<>();
      QuestionResponse questionResponse = new QuestionResponse();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

         if (authService.isRoleAdmin(authTokenModel.getAuthSecureId())) {
            response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, ADMIN, templateSecureId));
         } else {
            questionResponse.setTimeLimit(questionType == TRY_OUT_UKOM ? TIME_LIMIT_UKOM : TIME_LIMIT_SKB_GIZI);
            response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, CONSUMER, templateSecureId));
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createQuestion(String token, CreateQuestionRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.isValid()) {
            QuestionModel model = questionMapper.createRequestToModel(request);

            try {
               questionRepository.save(model);

               for (CreateAnswerRequest answer : request.getAnswers()) {
                  createAnswer(answer, model.getSecureId());
               }

               createResponse.setSecureId(model.getSecureId());

               response.setSuccess(createResponse);
            } catch (Exception e) {
               throw new AppException(e.toString());
            }
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateQuestion(String token, UpdateQuestionRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (request.isValid()) {
            for (UpdateAnswerRequest answer : request.getAnswers()) {
               try {
                  AnswerModel model = answerRepository.findBySecureId(answer.getSecureId());

                  answerRepository.save(answerMapper.updateRequestToModel(model, answer));
               } catch (Exception e) {
                  throw new AppException(e.toString());
               }
            }

            try {
               QuestionModel model = questionRepository.findBySecureId(request.getSecureId());

               questionRepository.save(questionMapper.updateRequestToModel(model, request));

               response.setSuccess(true);
            } catch (Exception e){
               throw new AppException(e.toString());
            }
         } else {
            throw new AppException(PARAMS_ERROR_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deleteQuestion(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         QuestionModel model = questionRepository.findBySecureId(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               questionRepository.save(model);

               response.setSuccess(true);
            } catch (Exception e){
               throw new AppException(e.toString());
            }
         } else {
            throw new AppException(NOT_FOUND_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private void createAnswer(CreateAnswerRequest request, String questionSecureId) {
      try {
         answerRepository.save(answerMapper.createRequestToModel(request, questionSecureId));
      } catch (Exception e){
         throw new AppException(e.toString());
      }
   }

   private QuestionResponse getTryOut(QuestionResponse questionResponse,
                                      int questionType,
                                      int questionSubType,
                                      int role,
                                      String templateSecureId
   ) {
      TemplateModel model = !templateSecureId.isEmpty()
              ? templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId)
              : templateService.getActiveTemplate(questionType, questionSubType);
      List<QuestionModel> questionModels = questionSubType == 0
              ? questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType)
              : questionRepository.findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(questionType, questionSubType, model.getSecureId());

      if (questionModels != null) {
         List<QuestionExamResponse> questionExamResponses = new ArrayList<>();

         for (QuestionModel question : questionModels) {
            QuestionExamResponse questionExamResponse = questionMapper.modelToQuestionExamResponse(question, role);
            List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(question.getSecureId());

            questionExamResponse.setAnswers(answerMapper.modelsToAnswerResponses(answerModels, role));
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
         List<QuestionModel> questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);

         if (questionModels != null) {
            int n = questionModels.size();

            if (questionType == QUIZ) {
               Collections.shuffle(questionModels);
               n = Math.min(questionModels.size(), 20);
            }

            for (int i=0; i<n; i++) {
               QuestionModel model = questionModels.get(i);
               QuestionAnswerResponse questionResponse = questionMapper.modelToQuestionValueResponse(model);
               List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(model.getSecureId());

               questionResponse.setAnswers(answerMapper.modelsToAnswerValueResponses(answerModels));
               questionResponses.add(questionResponse);
            }

            response.setSuccess(questionResponses);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

}
