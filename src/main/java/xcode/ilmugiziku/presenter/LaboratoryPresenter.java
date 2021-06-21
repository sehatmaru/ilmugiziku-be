package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;

import java.util.List;

public interface LaboratoryPresenter {
   BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList();
   BaseResponse<CreateBaseResponse> createLaboratoryValue(CreateLaboratoryValueRequest request);
   BaseResponse<Boolean> updateLaboratoryValue(UpdateLaboratoryValueRequest request);
   BaseResponse<Boolean> deleteLaboratoryValue(String secureId);
}
