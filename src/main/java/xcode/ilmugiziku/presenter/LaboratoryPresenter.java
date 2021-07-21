package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.laboratory.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.laboratory.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;

import java.util.List;

public interface LaboratoryPresenter {
   BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList(String token);
   BaseResponse<CreateBaseResponse> createLaboratoryValue(String token, CreateLaboratoryValueRequest request);
   BaseResponse<Boolean> updateLaboratoryValue(String token, UpdateLaboratoryValueRequest request);
   BaseResponse<Boolean> deleteLaboratoryValue(String token, String secureId);
}
