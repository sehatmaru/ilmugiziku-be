package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.question.QuestionValueResponse;
import xcode.ilmugiziku.mapper.AnswerMapper;
import xcode.ilmugiziku.mapper.QuestionMapper;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@Service
public class QuestionService implements QuestionPresenter {

   private final AuthTokenService authTokenService;
   private final AnswerService answerService;

   private final QuestionRepository questionRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   public QuestionService(AuthTokenService authTokenService, AnswerService answerService, QuestionRepository questionRepository) {
      this.authTokenService = authTokenService;
      this.questionRepository = questionRepository;
      this.answerService = answerService;
   }

   @Override
   public BaseResponse<List<QuestionValueResponse>> getQuizQuestions(String token) {
      return getQuiz(token, QUIZ);
   }

   @Override
   public BaseResponse<List<QuestionValueResponse>> getPracticeQuestions(String token) {
      return getQuiz(token, PRACTICE);
   }

   @Override
   public BaseResponse<List<QuestionResponse>> getTryOutQuestion(String token, int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
         if (questionSubType > 0 && questionSubType < 5) {
            response = getTryOut(token, questionType, questionSubType);
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

   private BaseResponse<List<QuestionResponse>> getTryOut(String token, int questionType, int questionSubType) {
      BaseResponse<List<QuestionResponse>> response = new BaseResponse<>();
      List<QuestionResponse> questionResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         List<QuestionModel> questionModels = new ArrayList<>();
         List<AnswerModel> answerModels = new ArrayList<>();

         try {
            if (questionSubType == 0) {
               questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);
            } else {
               questionModels = questionRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);
            }
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (questionModels != null) {
            for (QuestionModel question : questionModels) {
               QuestionResponse questionResponse = questionMapper.modelToQuestionResponse(question);

               try {
                  answerModels = answerService.getAnswerListByQuestionSecureId(question.getSecureId());
               } catch (Exception e) {
                  response.setFailed(e.toString());
               }

               questionResponse.setAnswers(answerMapper.modelsToAnswerResponses(answerModels));

               questionResponses.add(questionResponse);
            }

            response.setSuccess(questionResponses);
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private BaseResponse<List<QuestionValueResponse>> getQuiz(String token, int questionType) {
      BaseResponse<List<QuestionValueResponse>> response = new BaseResponse<>();
      List<QuestionValueResponse> questionResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         List<AnswerModel> answerModels = new ArrayList<>();

         List<QuestionModel> questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);

         if (questionModels != null) {
            for (QuestionModel question : questionModels) {
               QuestionValueResponse questionResponse = questionMapper.modelToQuestionValueResponse(question);

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
