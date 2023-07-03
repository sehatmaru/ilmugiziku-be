package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.DiscussionVideoRepository;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.exam.*;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.mapper.ExamMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.PFS;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.TimeLimitRefs.TIME_LIMIT_SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TimeLimitRefs.TIME_LIMIT_UKOM;

@Service
public class ExamService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthService authService;
   @Autowired private ScheduleService scheduleService;
   @Autowired private QuestionService questionService;
   @Autowired private TemplateService templateService;
   @Autowired private ExamRepository examRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private DiscussionVideoRepository discussionVideoRepository;
   @Autowired private QuestionRepository questionRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request) {
      BaseResponse<CreateExamResponse> response = new BaseResponse<>();

      AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            ScheduleModel schedule = scheduleService.getScheduleByDate(new Date());

            if (authService.getActiveAuthBySecureId(authTokenModel.getAuthSecureId()) != null) {
               if (request.getQuestionType() == QUIZ) {
                  CreateExamResponse createResponse = examMapper.generateResponse(request.getExams());

                  ExamModel model = examMapper.createRequestToModel(request, createResponse);
                  model.setAuthSecureId(authTokenModel.getAuthSecureId());
                  model.setScheduleSecureId(schedule.getSecureId());

                  try {
                     examRepository.save(calculateScore(request.getExams(), model));
                     response.setSuccess(createResponse);
                  } catch (Exception e) {
                     response.setFailed("");
                  }
               } else {
                  if (!isExamExist(schedule.getSecureId(), authTokenModel.getAuthSecureId(), request.getQuestionType(), request.getQuestionSubType())) {
                     CreateExamResponse createResponse = examMapper.generateResponse(request.getExams());

                     ExamModel model = examMapper.createRequestToModel(request, createResponse);
                     model.setAuthSecureId(authTokenModel.getAuthSecureId());
                     model.setScheduleSecureId(schedule.getSecureId());

                     try {
                        examRepository.save(calculateScore(request.getExams(), model));
                        response.setSuccess(createResponse);
                     } catch (Exception e) {
                        response.setFailed("");
                     }
                  } else {
                     response.setExistData("");
                  }
               }
            } else {
               response.setNotFound("");
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private ExamModel calculateScore(ExamRequest[] exams, ExamModel model) {
      int score = 0;
      int correct = 0;
      int incorrect = 0;

      for (ExamRequest exam: exams) {
         if (!exam.getAnswersSecureId().isEmpty()) {
            boolean res = false;

            for (AnswerModel answer: answerRepository.findAllByQuestionSecureId(exam.getQuestionsSecureId())) {
               if (answer.isValue()) {
                  if (answer.getSecureId().equals(exam.getAnswersSecureId())) {
                     res = true;
                  }
               }
            }

            if (res) {
               correct += 1;
            } else {
               incorrect += 1;
            }
         }
      }

      if (correct > 0) {
         double tmp = (float) correct / exams.length;
         score = (int) Math.round(tmp * 100);
      }

      model.setScore(score);
      model.setCorrect(correct);
      model.setIncorrect(incorrect);

      return model;
   }

   public BaseResponse<List<ExamResultResponse>> getExamResult(String token, int questionType) {
      BaseResponse<List<ExamResultResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         List<ExamModel> exams = examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionType(scheduleModel.getSecureId(), authTokenModel.getAuthSecureId(), questionType);

         if (authTokenModel.getAuthSecureId() != null && scheduleModel.getSecureId() != null) {
            response.setSuccess(examMapper.modelsToResultResponses(exams));
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<List<ExamRankResponse>> getExamRank(String token, int questionType, int questionSubType) {
      BaseResponse<List<ExamRankResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         List<ExamModel> exams = examRepository.findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(scheduleModel.getSecureId(), questionType, questionSubType);

         if (scheduleModel.getSecureId() != null) {
            List<ExamRankResponse> results = examMapper.modelsToRankResponses(exams);

            for (int i=0; i < exams.size(); i++) {
               AuthModel authModel = authService.getAuthBySecureId(exams.get(i).getAuthSecureId());
               results.get(i).setFullName(authModel.getFirstName() + ' ' + authModel.getLastName());
            }

            response.setSuccess(results);
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<List<ExamKeyResponse>> getExamKey(String token, int questionType, int questionSubType) {
      BaseResponse<List<ExamKeyResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         ExamModel exam = examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(scheduleModel.getSecureId(), authTokenModel.getAuthSecureId(), questionType, questionSubType);

         if (authTokenModel.getAuthSecureId() != null && scheduleModel.getSecureId() != null && exam != null) {
            List<ExamKeyResponse> results = new ArrayList<>();

            for (String questionSecureId: examMapper.stringToArray(exam.getQuestions())) {
               ExamKeyResponse examKeyResponse = new ExamKeyResponse();
               QuestionModel questionModel = questionRepository.findBySecureId(questionSecureId);
               List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(questionSecureId);

               for (AnswerModel model: answerModels) {
                  if (model.isValue()) {
                     examKeyResponse.setAnswer(model.getContent());
                     examKeyResponse.setQuestion(questionModel.getContent());
                     examKeyResponse.setDiscussion(questionModel.getDiscussion());
                  }
               }

               results.add(examKeyResponse);
            }

            response.setSuccess(results);
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<List<ExamInformationResponse>> getExamInformation(String token, int questionType) {
      BaseResponse<List<ExamInformationResponse>> response = new BaseResponse<>();
      List<ExamInformationResponse> results = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getActiveAuthBySecureId(authTokenModel.getAuthSecureId());
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());

         if (scheduleModel.getSecureId() != null && authModel.getSecureId() != null) {
            if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
               for (int i=1; i<PFS+1; i++) {
                  BaseResponse<QuestionResponse> questions = questionService.getTryOutQuestion(token, questionType, i, "");

                  results.add(new ExamInformationResponse(
                          i,
                          questions.getResult().getExam().size(),
                          questionType == TRY_OUT_UKOM ? TIME_LIMIT_UKOM : TIME_LIMIT_SKB_GIZI,
                          !isExamExist(scheduleModel.getSecureId(), authModel.getSecureId(), questionType, i)
                  ));
               }

               response.setSuccess(results);
            } else {
               response.setWrongParams();
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<List<ExamVideoResponse>> getExamVideo(String token, int questionType) {
      BaseResponse<List<ExamVideoResponse>> response = new BaseResponse<>();
      List<ExamVideoResponse> results = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());

         if (authTokenModel.getAuthSecureId() != null && scheduleModel.getSecureId() != null) {
            if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
               for (int i=1; i<PFS+1; i++) {
                  ExamModel exam = examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(scheduleModel.getSecureId(), authTokenModel.getAuthSecureId(), questionType, i);
                  TemplateModel template = templateService.getActiveTemplate(questionType, i);

                  if (exam != null && template != null) {
                     DiscussionVideoModel video = discussionVideoRepository.findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(questionType, i, template.getSecureId());

                     if (video != null) {
                        results.add(new ExamVideoResponse(video.getUri(), i));
                     }
                  }
               }

               response.setSuccess(results);
            } else {
               response.setWrongParams();
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private boolean isExamExist(String schedule, String auth, int questionType, int questionSubType) {
      return examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(schedule, auth, questionType, questionSubType) != null;
   }
}
