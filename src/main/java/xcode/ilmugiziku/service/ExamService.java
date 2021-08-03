package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.exam.CreateExamResponse;
import xcode.ilmugiziku.domain.response.exam.ExamKeyResponse;
import xcode.ilmugiziku.domain.response.exam.ExamRankResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;
import xcode.ilmugiziku.mapper.ExamMapper;
import xcode.ilmugiziku.presenter.ExamPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class ExamService implements ExamPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final AnswerService answerService;
   private final ScheduleService scheduleService;
   private final QuestionService questionService;

   private final ExamRepository examRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public ExamService(AuthTokenService authTokenService,
                      AuthService authService,
                      AnswerService answerService,
                      ScheduleService scheduleService,
                      QuestionService questionService,
                      ExamRepository examRepository) {
      this.authTokenService = authTokenService;
      this.authService = authService;
      this.examRepository = examRepository;
      this.answerService = answerService;
      this.scheduleService = scheduleService;
      this.questionService = questionService;
   }

   @Override
   public BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request) {
      BaseResponse<CreateExamResponse> response = new BaseResponse<>();

      AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            ScheduleModel schedule = scheduleService.getScheduleByDate(new Date());

            if (authService.getActiveAuthBySecureId(authTokenModel.getAuthSecureId()) != null) {
               if (!isExamExist(schedule.getSecureId(), authTokenModel.getAuthSecureId(), request.getQuestionType(), request.getQuestionSubType())) {
                  CreateExamResponse createResponse = examMapper.generateResponse(request.getExams());

                  ExamModel model = examMapper.createRequestToModel(request, createResponse);
                  model.setAuthSecureId(authTokenModel.getAuthSecureId());
                  model.setScheduleSecureId(schedule.getSecureId());

                  try {
                     examRepository.save(calculateScore(request.getExams(), model));
                  } catch (Exception e) {
                     response.setFailed("");
                  }

                  response.setSuccess(createResponse);
               } else {
                  response.setExistData("");
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

            for (AnswerModel answer: answerService.getAnswerListByQuestionSecureId(exam.getQuestionsSecureId())) {
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

   @Override
   public BaseResponse<List<ExamResultResponse>> getExamResult(String token, int questionType) {
      BaseResponse<List<ExamResultResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         List<ExamModel> exams = examRepository.findByScheduleSecureIdAndAuthSecureId(scheduleModel.getSecureId(), authTokenModel.getAuthSecureId());

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

   @Override
   public BaseResponse<List<ExamRankResponse>> getExamRank(String token, int questionType, int questionSubType) {
      BaseResponse<List<ExamRankResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         List<ExamModel> exams = examRepository.findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(scheduleModel.getSecureId(), questionType, questionSubType);

         if (scheduleModel.getSecureId() != null) {
            List<ExamRankResponse> results = examMapper.modelsToRankResponses(exams);

            for (int i=0; i < exams.size(); i++) {
               AuthModel authModel = authService.getAuthBySecureId(exams.get(i).getAuthSecureId());
               results.get(i).setEmail(authModel.getEmail());
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

   @Override
   public BaseResponse<List<ExamKeyResponse>> getExamKey(String token, int questionType, int questionSubType) {
      BaseResponse<List<ExamKeyResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
         ExamModel exam = examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(scheduleModel.getSecureId(), authTokenModel.getAuthSecureId(), questionType, questionSubType);

         if (authTokenModel.getAuthSecureId() != null && scheduleModel.getSecureId() != null && exam.getSecureId() != null) {
            List<ExamKeyResponse> results = new ArrayList<>();

            for (String questionSecureId: examMapper.stringToArray(exam.getQuestions())) {
               ExamKeyResponse examKeyResponse = new ExamKeyResponse();
               QuestionModel questionModel = questionService.getQuestionBySecureId(questionSecureId);
               List<AnswerModel> answerModels = answerService.getAnswerListByQuestionSecureId(questionSecureId);

               for (AnswerModel model: answerModels) {
                  if (model.isValue()) {
                     examKeyResponse.setAnswer(model.getContent());
                     examKeyResponse.setQuestion(questionModel.getContent());
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

   private boolean isExamExist(String schedule, String auth, int questionType, int questionSubType) {
      return examRepository.findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(schedule, auth, questionType, questionSubType) != null;
   }
}
