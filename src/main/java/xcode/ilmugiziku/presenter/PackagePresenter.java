package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.pack.CreatePackageRequest;
import xcode.ilmugiziku.domain.request.pack.UpdatePackageRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;

import java.util.List;

public interface PackagePresenter {
   BaseResponse<List<PackageResponse>> getPackageList(String token);
   BaseResponse<PackageResponse> getPackage(String token, String secureId);
   BaseResponse<CreateBaseResponse> createPackage(String token, CreatePackageRequest request);
   BaseResponse<Boolean> updatePackage(String token, String secureId, UpdatePackageRequest request);
   BaseResponse<Boolean> deletePackage(String token, String secureId);
}
