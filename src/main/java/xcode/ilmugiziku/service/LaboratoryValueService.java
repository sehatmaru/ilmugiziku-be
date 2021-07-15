package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.LaboratoryValueModel;
import xcode.ilmugiziku.domain.repository.LaboratoryValueRepository;
import xcode.ilmugiziku.domain.request.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.presenter.LaboratoryPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class LaboratoryValueService implements LaboratoryPresenter {

   private final AuthTokenService authTokenService;

   private final LaboratoryValueRepository laboratoryValueRepository;

   public LaboratoryValueService(AuthTokenService authTokenService, LaboratoryValueRepository laboratoryValueRepository) {
      this.authTokenService = authTokenService;
      this.laboratoryValueRepository = laboratoryValueRepository;
   }

   @Override
   public BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList(String token) {
      BaseResponse<List<LaboratoryValueResponse>> response = new BaseResponse<>();
      List<LaboratoryValueResponse> laboratoryValueResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         try {
            List<LaboratoryValueModel> models = laboratoryValueRepository.findByDeletedAtIsNull();

            for (LaboratoryValueModel model : models) {
               LaboratoryValueResponse value = new LaboratoryValueResponse();
               value.setSecureId(model.getSecureId());
               value.setContent(model.getContent());
               value.setValue(model.getValue());

               laboratoryValueResponses.add(value);
            }

            response.setSuccess(laboratoryValueResponses);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createLaboratoryValue(String token, CreateLaboratoryValueRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         String tempSecureId = generateSecureId();

         LaboratoryValueModel model = new LaboratoryValueModel();
         model.setSecureId(tempSecureId);
         model.setContent(request.getContent());
         model.setValue(request.getValue());
         model.setCreatedAt(new Date());

         try {
            laboratoryValueRepository.save(model);

            createResponse.setSecureId(tempSecureId);

            response.setSuccess(createResponse);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateLaboratoryValue(String token, UpdateLaboratoryValueRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      LaboratoryValueModel model = new LaboratoryValueModel();

      if (authTokenService.isValidToken(token)) {
         try {
            model = laboratoryValueRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
         } catch (Exception e) {
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }

         model.setContent(request.getContent());
         model.setValue(request.getValue());
         model.setUpdatedAt(new Date());

         try {
            laboratoryValueRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteLaboratoryValue(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      LaboratoryValueModel model = new LaboratoryValueModel();

      if (authTokenService.isValidToken(token)) {
         try {
            model = laboratoryValueRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               laboratoryValueRepository.save(model);

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

}
