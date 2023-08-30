package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.model.UserExamRelModel;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.repository.UserExamRepository;
import xcode.ilmugiziku.domain.request.exam.CreateUpdateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamResultRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.exam.*;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.ExamMapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class ExamService {

   @Autowired private QuestionService questionService;
   @Autowired private ProfileService profileService;
   @Autowired private ExamRepository examRepository;
   @Autowired private TemplateRepository templateRepository;
   @Autowired private UserExamRepository userExamRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public BaseResponse<List<ExamListResponse>> getExamList(String title, String status) {
      BaseResponse<List<ExamListResponse>> response = new BaseResponse<>();

      try {
         List<ExamModel> models = examRepository.findAllByDeletedAtIsNull();

         models = models.stream()
                 .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                 .collect(Collectors.toList());

         if (!status.isEmpty()) {
            boolean available = Objects.equals(status, "available");

            models = models.stream()
                    .filter(e -> e.isAvailable() == available)
                    .collect(Collectors.toList());
         }

         response.setSuccess(examMapper.modelsToListResponses(models));
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<ExamResponse> getExamDetail(String secureId) {
      BaseResponse<ExamResponse> response = new BaseResponse<>();

      ExamModel model = examRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         response.setSuccess(examMapper.modelToResponse(model));
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * cancel to participate in the exam,
    * the exam must be not started yet
    * and subtract the current participation
    * @param examSecureId string
    * @return boolean
    */
   public BaseResponse<Boolean> cancel(String examSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel examModel = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      UserExamRelModel userExam = userExamRepository.getUserExam(CurrentUser.get().getUserSecureId(), examSecureId);

      if (examModel == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (userExam == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
      if (examModel.getStartAt().before(new Date()) || examModel.getEndAt().after(new Date())) throw new AppException(EXAM_CANT_CANCEL);

      try {
         userExam.setDeleted(true);

         examModel.setCurrentParticipant(examModel.getCurrentParticipant()-1);

         examRepository.save(examModel);
         userExamRepository.save(userExam);

         response.setSuccess(true);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * get the exam result,
    * will succeed if user already finished
    * the exam
    * @param examSecureId string
    * @return response
    */
   public BaseResponse<ExamResultResponse> getExamResult(String examSecureId) {
      BaseResponse<ExamResultResponse> response = new BaseResponse<>();

      ExamModel examModel = examRepository.findBySecureId(examSecureId);
      UserExamRelModel userExam = userExamRepository.getUserExam(CurrentUser.get().getUserSecureId(), examSecureId);

      if (examModel == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (userExam == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
      if (userExam.getFinishTime() == null) throw new AppException(USER_EXAM_NOT_FOUND);

      try {
         ExamResultResponse result = new ExamResultResponse();
         result.setDuration(userExam.getDuration());
         result.setTitle(examModel.getTitle());
         result.setBlank(userExam.getBlank());
         result.setIncorrect(userExam.getIncorrect());
         result.setCorrect(userExam.getCorrect());
         result.setScore(userExam.getScore());

         response.setSuccess(result);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }
   
   public BaseResponse<CreateBaseResponse> createExam(CreateUpdateExamRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         ExamModel model = examMapper.createRequestToModel(request);
         examRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateExam(String examSecureId, CreateUpdateExamRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel model = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         examRepository.save(examMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * delete the exam,
    * the exam must be not started yet
    * @param secureId string
    * @return boolean
    */
   public BaseResponse<Boolean> deleteExam(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel model = examRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            examRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   /**
    * set exam template
    * @param examSecureId string
    * @param templateSecureId string
    * @return boolean
    */
   public BaseResponse<Boolean> setTemplate(String examSecureId, String templateSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      TemplateModel template = templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId);

      if (exam == null || template == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         exam.setTemplate(templateSecureId);
         exam.setUpdatedAt(new Date());
         examRepository.save(exam);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }


      return response;
   }

   /**
    * @deprecated not use it anymore,
    * must set time on create exam
    * @param examSecureId string
    * @param startTime date
    * @param endTime date
    * @return boolean
    */
   @Deprecated
   public BaseResponse<Boolean> setTime(String examSecureId, Date startTime, Date endTime) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);

      if (exam == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         exam.setStartAt(startTime);
         exam.setEndAt(endTime);
         exam.setUpdatedAt(new Date());
         examRepository.save(exam);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }


      return response;
   }

   /**
    * apply for join the exam,
    * the exam must be not started yet
    * and there is still slot available
    * @param examSecureId string
    * @return boolean
    */
   public BaseResponse<Boolean> apply(String examSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      UserExamRelModel prevUserExam = userExamRepository.getUserExam(CurrentUser.get().getUserSecureId(), examSecureId);

      if (exam == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (exam.isFull()) throw new AppException(EXAM_FULL);
      if (prevUserExam != null) throw new AppException(USER_WEBINAR_EXIST);

      try {
         UserExamRelModel userExam = new UserExamRelModel();
         userExam.setSecureId(generateSecureId());
         userExam.setExam(examSecureId);
         userExam.setUser(CurrentUser.get().getUserSecureId());

         userExamRepository.save(userExam);

         exam.setCurrentParticipant(exam.getCurrentParticipant()+1);

         if (exam.getCurrentParticipant() == exam.getMaxParticipant()) exam.setAvailable(false);
         examRepository.save(exam);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }


      return response;
   }

   /**
    * start the exam to retrieve the questions
    * @param examSecureId string
    * @return response
    */
   public BaseResponse<DoExamResponse> startExam(String examSecureId) {
      BaseResponse<DoExamResponse> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      UserExamRelModel userExam = userExamRepository.getUserExam(CurrentUser.get().getUserSecureId(), examSecureId);

      if (exam == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (exam.getStartAt().after(new Date())) throw new AppException(EXAM_NOT_STARTED_YET);
      if (exam.getEndAt().before(new Date())) throw new AppException(EXAM_HAS_ENDED);
      if (userExam == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
      if (userExam.getFinishTime() != null) throw new AppException(USER_EXAM_EXIST);

      try {
         DoExamResponse examResponse = new DoExamResponse();
         examResponse.setTitle(exam.getTitle());
         examResponse.setDuration(exam.getDuration());
         examResponse.setQuestions(questionService.getQuestionsByTemplate(exam.getTemplate()));

         userExam.setStartTime(new Date());
         userExamRepository.save(userExam);

         response.setSuccess(examResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * finish the exam to send the answer
    * and calculated
    * @param examSecureId string
    * @param request body
    * @return response
    */
   public BaseResponse<ExamResultResponse> finishExam(String examSecureId, List<ExamResultRequest> request) {
      BaseResponse<ExamResultResponse> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      UserExamRelModel userExam = userExamRepository.getUserExam(CurrentUser.get().getUserSecureId(), examSecureId);

      if (exam == null) throw new AppException(NOT_FOUND_MESSAGE);
      if (userExam == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);

      try {
         ExamResultResponse examResponse = questionService.calculateScore(request);
         examResponse.setTitle(exam.getTitle());
         examResponse.setDuration((int) ((new Date().getTime()-userExam.getStartTime().getTime())/60000));

         userExam.setFinishTime(new Date());
         userExam.setBlank(examResponse.getBlank());
         userExam.setCorrect(examResponse.getCorrect());
         userExam.setIncorrect(examResponse.getIncorrect());
         userExam.setScore(examResponse.getScore());
         userExam.setDuration(examResponse.getDuration());

         userExamRepository.save(userExam);

         refreshRank(examSecureId);

         response.setSuccess(examResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<ExamRankResponse> getExamRank(String examSecureId) {
      BaseResponse<ExamRankResponse> response = new BaseResponse<>();

      ExamModel exam = examRepository.findBySecureIdAndDeletedAtIsNull(examSecureId);
      List<UserExamRelModel> userExam = userExamRepository.getUserExamRank(examSecureId);

      if (exam == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         ExamRankResponse result = new ExamRankResponse();
         result.setTitle(exam.getTitle());
         result.setParticipant(userExam.size());

         for (UserExamRelModel model : userExam) {
            RankResponse rankResponse = examMapper.modelToRankResponse(model, profileService.getUserFullName(model.getUser()));
            result.getRanks().add(rankResponse);
         }

         response.setSuccess(result);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * recalculate rank position
    * @param examSecureId string
    */
   private void refreshRank(String examSecureId) {
      List<UserExamRelModel> userExam = userExamRepository.getUserExamRank(examSecureId);

      int rank = 1;

      for (UserExamRelModel model : userExam) {
         model.setRanking(rank);
         rank++;
      }

      userExamRepository.saveAll(userExam);
   }
}
