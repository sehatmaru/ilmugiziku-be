package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.packagefeature.CreatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.packagefeature.UpdatePackageFeatureRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PackageFeatureResponse;

import java.util.List;

public interface PackageFeaturePresenter {
   BaseResponse<List<PackageFeatureResponse>> getPackageFeatureList(String token);
   BaseResponse<CreateBaseResponse> createPackageFeature(String token, CreatePackageFeatureRequest request);
   BaseResponse<Boolean> updatePackageFeature(String token, String secureId, UpdatePackageFeatureRequest request);
   BaseResponse<Boolean> deletePackageFeature(String token, String secureId);
}
