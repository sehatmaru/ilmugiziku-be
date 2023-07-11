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
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.ExamMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class ExamService {

   @Autowired private ExamRepository examRepository;
   @Autowired private TemplateRepository templateRepository;
   @Autowired private UserExamRepository userExamRepository;

   private final ExamMapper examMapper = new ExamMapper();

   public BaseResponse<List<ExamResponse>> getExamList() {
      BaseResponse<List<ExamResponse>> response = new BaseResponse<>();

      try {
         List<ExamModel> models = examRepository.findAllByDeletedAtIsNull();

         response.setSuccess(examMapper.modelsToResponses(models));
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
}
