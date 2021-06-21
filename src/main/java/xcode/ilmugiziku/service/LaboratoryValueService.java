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

   final LaboratoryValueRepository laboratoryValueRepository;

   public LaboratoryValueService(LaboratoryValueRepository laboratoryValueRepository) {
      this.laboratoryValueRepository = laboratoryValueRepository;
   }

   @Override
   public BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList() {
      BaseResponse<List<LaboratoryValueResponse>> response = new BaseResponse<>();
      List<LaboratoryValueResponse> laboratoryValueResponses = new ArrayList<>();

      try {
         List<LaboratoryValueModel> models = laboratoryValueRepository.findByDeletedAtIsNull();

         for (LaboratoryValueModel model : models) {
            LaboratoryValueResponse value = new LaboratoryValueResponse();
            value.setSecureId(model.getSecureId());
            value.setContent(model.getContent());
            value.setValue(model.getValue());

            laboratoryValueResponses.add(value);
         }

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);
         response.setResult(laboratoryValueResponses);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(e.toString());
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createLaboratoryValue(CreateLaboratoryValueRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      String tempSecureId = generateSecureId();

      LaboratoryValueModel model = new LaboratoryValueModel();
      model.setSecureId(tempSecureId);
      model.setContent(request.getContent());
      model.setValue(request.getValue());
      model.setCreatedAt(new Date());

      try {
         laboratoryValueRepository.save(model);

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);
         createResponse.setSecureId(tempSecureId);

         response.setResult(createResponse);
      } catch (Exception e){
         response.setStatusCode(FAILED_CODE);
         response.setMessage(FAILED_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateLaboratoryValue(UpdateLaboratoryValueRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      LaboratoryValueModel model = new LaboratoryValueModel();

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

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);

         response.setResult(true);
      } catch (Exception e){
         response.setStatusCode(FAILED_CODE);
         response.setMessage(FAILED_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteLaboratoryValue(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      LaboratoryValueModel model = new LaboratoryValueModel();

      try {
         model = laboratoryValueRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(FAILED_MESSAGE);
      }

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            laboratoryValueRepository.save(model);

            response.setStatusCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);

            response.setResult(true);
         } catch (Exception e){
            response.setStatusCode(FAILED_CODE);
            response.setMessage(FAILED_MESSAGE);
         }
      } else {
         response.setStatusCode(NOT_FOUND_CODE);
         response.setMessage(NOT_FOUND_MESSAGE);
      }

      return response;
   }

}
