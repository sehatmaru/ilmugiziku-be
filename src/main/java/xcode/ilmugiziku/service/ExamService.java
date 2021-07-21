package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.request.CreateExamRequest;
import xcode.ilmugiziku.domain.request.ExamRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateExamResponse;
import xcode.ilmugiziku.mapper.ExamMapper;
import xcode.ilmugiziku.presenter.ExamPresenter;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class ExamService implements ExamPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final AnswerService answerService;

   private final ExamRepository examRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public ExamService(AuthTokenService authTokenService, AuthService authService, AnswerService answerService, ExamRepository examRepository) {
      this.authTokenService = authTokenService;
      this.authService = authService;
      this.examRepository = examRepository;
      this.answerService = answerService;
   }

   @Override
   public BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request) {
      BaseResponse<CreateExamResponse> response = new BaseResponse<>();

      AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            if (authService.getActiveAuthBySecureId(authTokenModel.getAuthSecureId()) != null) {
               CreateExamResponse createResponse = scoreCounter(request.getExams());

               ExamModel model = examMapper.createRequestToModel(request, createResponse);
               model.setAuthSecureId(authTokenModel.getAuthSecureId());

               try {
                  examRepository.save(model);
               } catch (Exception e) {
                  response.setFailed("");
               }

               response.setSuccess(createResponse);
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

   private CreateExamResponse scoreCounter(ExamRequest[] exams) {
      CreateExamResponse result = new CreateExamResponse();

      int score = 0;
      int blank = 0;
      int correct = 0;
      int incorrect = 0;

      for (ExamRequest exam: exams) {
         if (exam.getAnswersSecureId().isEmpty()) {
            blank += 1;
         } else {
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

      result.setSecureId(generateSecureId());
      result.setScore(score);
      result.setBlank(blank);
      result.setCorrect(correct);
      result.setIncorrect(incorrect);

      return result;
   }
}
