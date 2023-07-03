package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.LaboratoryValueModel;
import xcode.ilmugiziku.domain.repository.LaboratoryValueRepository;
import xcode.ilmugiziku.domain.request.laboratory.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.laboratory.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.mapper.LaboratoryValueMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class LaboratoryValueService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private LaboratoryValueRepository laboratoryValueRepository;

   private final LaboratoryValueMapper laboratoryValueMapper = new LaboratoryValueMapper();

   public BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList(String token) {
      BaseResponse<List<LaboratoryValueResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         try {
            List<LaboratoryValueModel> models = laboratoryValueRepository.findByDeletedAtIsNull();

            response.setSuccess(laboratoryValueMapper.modelsToResponses(models));
         } catch (Exception e) {
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createLaboratoryValue(String token, CreateLaboratoryValueRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         LaboratoryValueModel model = laboratoryValueMapper.createRequestToModel(request);

         try {
            laboratoryValueRepository.save(model);

            createResponse.setSecureId(model.getSecureId());

            response.setSuccess(createResponse);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

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

         try {
            laboratoryValueRepository.save(laboratoryValueMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      }

      return response;
   }

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
