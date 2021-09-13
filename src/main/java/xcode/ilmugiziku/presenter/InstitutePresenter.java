package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.institution.CreateInstituteRequest;
import xcode.ilmugiziku.domain.request.institution.UpdateInstituteRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.InstituteResponse;

import java.util.List;

public interface InstitutePresenter {
   BaseResponse<List<InstituteResponse>> getInstitutionList(String token);
   BaseResponse<CreateBaseResponse> createInstitute(String token, CreateInstituteRequest request);
   BaseResponse<Boolean> updateInstitute(String token, UpdateInstituteRequest request);
   BaseResponse<Boolean> deleteInstitute(String token, String secureId);
}
