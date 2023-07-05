package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.template.CreateTemplateRequest;
import xcode.ilmugiziku.domain.request.template.UpdateTemplateRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TemplateResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.TemplateMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.QuestionTypeEnum.TRY_OUT_SKB;
import static xcode.ilmugiziku.domain.enums.QuestionTypeEnum.TRY_OUT_UKOM;
import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class TemplateService {

   @Autowired private TemplateRepository templateRepository;

   private final TemplateMapper templateMapper = new TemplateMapper();

   public BaseResponse<List<TemplateResponse>> getTemplateList(QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType) {
      BaseResponse<List<TemplateResponse>> response = new BaseResponse<>();

      if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB) {
         List<TemplateModel> models = templateRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);

         response.setSuccess(templateMapper.modelsToResponses(models));
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> setTemplateActive(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (templateModel != null) {
         List<TemplateModel> list = templateRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(templateModel.getQuestionType(), templateModel.getQuestionSubType());

         for (TemplateModel template : list) {
            template.setUsed(template.getSecureId().equals(secureId));

            templateRepository.save(template);
         }

         response.setSuccess(true);
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createTemplate(CreateTemplateRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         TemplateModel model = templateMapper.createRequestToModel(request);
         templateRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateTemplate(UpdateTemplateRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
         templateRepository.save(templateMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTemplate(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            templateRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public TemplateModel getActiveTemplate(QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType) {
      TemplateModel result = new TemplateModel();
      List<TemplateModel> list = templateRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);

      for (TemplateModel model : list) {
         if (model.isUsed()) {
            result = model;
         }
      }

      return result;
   }
}
