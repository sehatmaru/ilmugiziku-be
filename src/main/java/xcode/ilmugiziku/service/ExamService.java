package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateExamResponse;
import xcode.ilmugiziku.mapper.ExamMapper;
import xcode.ilmugiziku.presenter.ExamPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class ExamService implements ExamPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final AnswerService answerService;
   private final ScheduleService scheduleService;

   private final ExamRepository examRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public ExamService(AuthTokenService authTokenService, AuthService authService, AnswerService answerService, ScheduleService scheduleService, ExamRepository examRepository) {
      this.authTokenService = authTokenService;
      this.authService = authService;
      this.examRepository = examRepository;
      this.answerService = answerService;
      this.scheduleService = scheduleService;
   }

   @Override
   public BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request) {
      BaseResponse<CreateExamResponse> response = new BaseResponse<>();

      AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            ScheduleModel schedule = scheduleService.getScheduleByDateAndAuthSecureId(new Date(), authTokenModel.getAuthSecureId());

            if (authService.getActiveAuthBySecureId(authTokenModel.getAuthSecureId()) != null
                    && schedule.getSecureId() != null) {
               if (!isExamExist(schedule.getSecureId(), authTokenModel.getAuthSecureId())) {
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

   private boolean isExamExist(String schedule, String auth) {
      return examRepository.findByScheduleSecureIdAndAuthSecureId(schedule, auth) != null;
   }
}
