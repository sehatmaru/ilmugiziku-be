package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.AnswerMapper;
import xcode.ilmugiziku.mapper.QuestionMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.PARAMS_ERROR_MESSAGE;

@Service
public class QuestionService {

   @Autowired private UserService userService;
   @Autowired private TemplateService templateService;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private TemplateRepository templateRepository;

   private final QuestionMapper questionMapper = new QuestionMapper();
   private final AnswerMapper answerMapper = new AnswerMapper();

   // TODO: 11/07/23
//   public BaseResponse<List<QuestionAnswerResponse>> getQuizQuestions() {
//      return getQuiz(QUIZ);
//   }
//
//   public BaseResponse<List<QuestionAnswerResponse>> getPracticeQuestions() {
//      return getQuiz(PRACTICE);
//   }
//
//   public BaseResponse<QuestionResponse> getTryOutQuestion(QuestionTypeEnum questionType,
//                                                           QuestionSubTypeEnum questionSubType,
//                                                           String templateSecureId
//   ) {
//      BaseResponse<QuestionResponse> response = new BaseResponse<>();
//      QuestionResponse questionResponse = new QuestionResponse();
//
//      if (userService.isRoleAdmin(CurrentUser.get().getUserSecureId())) {
//         response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, ADMIN, templateSecureId));
//      } else {
//         questionResponse.setTimeLimit(questionType == TRY_OUT_UKOM ? TIME_LIMIT_UKOM : TIME_LIMIT_SKB);
//         response.setSuccess(getTryOut(questionResponse, questionType, questionSubType, CONSUMER, templateSecureId));
//      }
//
//      return response;
//   }

   // TODO: 11/07/23
//   public BaseResponse<CreateBaseResponse> createQuestion(CreateQuestionRequest request) {
//      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
//      CreateBaseResponse createResponse = new CreateBaseResponse();
//
//      if (request.isValid()) {
//         QuestionModel model = questionMapper.createRequestToModel(request);
//
//         try {
//            questionRepository.save(model);
//
//            for (CreateAnswerRequest answer : request.getAnswers()) {
//               createAnswer(answer, model.getSecureId());
//            }
//
//            createResponse.setSecureId(model.getSecureId());
//
//            response.setSuccess(createResponse);
//         } catch (Exception e) {
//            throw new AppException(e.toString());
//         }
//      }
//
//      return response;
//   }

   // TODO: 11/07/23  
//   public BaseResponse<Boolean> updateQuestion(UpdateQuestionRequest request) {
//      BaseResponse<Boolean> response = new BaseResponse<>();
//
//      if (request.isValid()) {
//         for (UpdateAnswerRequest answer : request.getAnswers()) {
//            try {
//               AnswerModel model = answerRepository.findBySecureId(answer.getSecureId());
//
//               answerRepository.save(answerMapper.updateRequestToModel(model, answer));
//            } catch (Exception e) {
//               throw new AppException(e.toString());
//            }
//         }
//
//         try {
//            QuestionModel model = questionRepository.findBySecureId(request.getSecureId());
//
//            questionRepository.save(questionMapper.updateRequestToModel(model, request));
//
//            response.setSuccess(true);
//         } catch (Exception e){
//            throw new AppException(e.toString());
//         }
//      } else {
//         throw new AppException(PARAMS_ERROR_MESSAGE);
//      }
//
//      return response;
//   }

   public BaseResponse<Boolean> deleteQuestion(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      QuestionModel model = questionRepository.findBySecureId(secureId);

      if (model != null) {
//         model.setDeletedAt(new Date());

         try {
            questionRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
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

   // TODO: 11/07/23
//   private QuestionResponse getTryOut(QuestionResponse questionResponse,
//                                      QuestionTypeEnum questionType,
//                                      QuestionSubTypeEnum questionSubType,
//                                      RoleEnum role,
//                                      String templateSecureId
//   ) {
//      TemplateModel model = !templateSecureId.isEmpty()
//              ? templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId)
//              : templateService.getActiveTemplate(questionType, questionSubType);
//
//      if (model == null) {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      List<QuestionModel> questionModels = questionSubType == QuestionSubTypeEnum.NONE
//              ? questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType)
//              : questionRepository.findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(questionType, questionSubType, model.getSecureId());
//
//      if (questionModels != null) {
//         List<QuestionExamResponse> questionExamResponses = new ArrayList<>();
//
//         for (QuestionModel question : questionModels) {
//            QuestionExamResponse questionExamResponse = questionMapper.modelToQuestionExamResponse(question, role);
//            List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(question.getSecureId());
//
//            questionExamResponse.setAnswers(answerMapper.modelsToAnswerResponses(answerModels, role));
//            questionExamResponses.add(questionExamResponse);
//         }
//
//         questionResponse.setExam(questionExamResponses);
//      }
//
//      return questionResponse;
//   }
//
//   private BaseResponse<List<QuestionAnswerResponse>> getQuiz(QuestionTypeEnum questionType) {
//      BaseResponse<List<QuestionAnswerResponse>> response = new BaseResponse<>();
//      List<QuestionAnswerResponse> questionResponses = new ArrayList<>();
//
//      List<QuestionModel> questionModels = questionRepository.findByQuestionTypeAndDeletedAtIsNull(questionType);
//
//      if (questionModels != null) {
//         int n = questionModels.size();
//
//         if (questionType == QUIZ) {
//            Collections.shuffle(questionModels);
//            n = Math.min(questionModels.size(), 20);
//         }
//
//         for (int i=0; i<n; i++) {
//            QuestionModel model = questionModels.get(i);
//            QuestionAnswerResponse questionResponse = questionMapper.modelToQuestionValueResponse(model);
//            List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(model.getSecureId());
//
//            questionResponse.setAnswers(answerMapper.modelsToAnswerValueResponses(answerModels));
//            questionResponses.add(questionResponse);
//         }
//
//         response.setSuccess(questionResponses);
//      }
//
//      return response;
//   }

}
