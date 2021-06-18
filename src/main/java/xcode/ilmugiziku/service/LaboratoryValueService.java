package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.LaboratoryValueModel;
import xcode.ilmugiziku.domain.repository.LaboratoryValueRepository;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.presenter.LaboratoryPresenter;

import java.util.ArrayList;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;

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

}
