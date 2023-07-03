package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.template.CreateTemplateRequest;
import xcode.ilmugiziku.domain.request.template.UpdateTemplateRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TemplateResponse;
import xcode.ilmugiziku.mapper.TemplateMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.TRY_OUT_SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.TRY_OUT_UKOM;

@Service
public class TemplateService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private TemplateRepository templateRepository;

   private final TemplateMapper templateMapper = new TemplateMapper();

   public BaseResponse<List<TemplateResponse>> getTemplateList(String token, int questionType, int questionSubType) {
      BaseResponse<List<TemplateResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB_GIZI) {
            List<TemplateModel> models = templateRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(questionType, questionSubType);

            response.setSuccess(templateMapper.modelsToResponses(models));
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> setTemplateActive(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      AuthTokenModel model = authTokenService.getAuthTokenByToken(token);
      TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null && templateModel != null) {
         List<TemplateModel> list = templateRepository.findByQuestionTypeAndQuestionSubTypeAndDeletedAtIsNull(templateModel.getQuestionType(), templateModel.getQuestionSubType());

         for (TemplateModel template : list) {
            template.setUsed(template.getSecureId().equals(secureId));

            templateRepository.save(template);
         }

         response.setSuccess(true);
      } else {
         response.setNotFound("");
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createTemplate(String token, CreateTemplateRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               TemplateModel model = templateMapper.createRequestToModel(request);
               templateRepository.save(model);

               createResponse.setSecureId(model.getSecureId());

               response.setSuccess(createResponse);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateTemplate(String token, UpdateTemplateRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());

         try {
            templateRepository.save(templateMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTemplate(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               templateRepository.save(model);

               response.setSuccess(true);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public TemplateModel getActiveTemplate(int questionType, int questionSubType) {
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
