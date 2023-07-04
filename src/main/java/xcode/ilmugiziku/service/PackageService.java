package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.PackageFeatureModel;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.repository.PackageFeatureRepository;
import xcode.ilmugiziku.domain.repository.PackageRepository;
import xcode.ilmugiziku.domain.repository.UserRepository;
import xcode.ilmugiziku.domain.request.pack.CreatePackageRequest;
import xcode.ilmugiziku.domain.request.pack.UpdatePackageRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.pack.PackageFeatureResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.PackageMapper;

import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class PackageService {

   @Autowired private PackageRepository packageRepository;
   @Autowired private PackageFeatureRepository packageFeatureRepository;
   @Autowired private UserRepository userRepository;

   private final PackageMapper packageMapper = new PackageMapper();

   public BaseResponse<List<PackageResponse>> getPackageList() {
      BaseResponse<List<PackageResponse>> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getSecureId());
      List<PackageModel> models = packageRepository.findByDeletedAtIsNull();
      List<PackageResponse> responses = packageMapper.modelsToResponses(models);

      for (PackageResponse resp : responses) {
         resp.setOpen(!userModel.isPaidPackage(resp.getPackageType()));

         for (PackageFeatureResponse feature: resp.getFeatures()) {
            PackageFeatureModel model = packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(feature.getSecureId());

            feature.setDesc(model.getDescription());
         }
      }

      response.setSuccess(responses);

      return response;
   }

   public BaseResponse<PackageResponse> getPackage(String secureId) {
      BaseResponse<PackageResponse> response = new BaseResponse<>();

      PackageModel model = packageRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      PackageResponse result = packageMapper.modelToResponse(model);

      for (PackageFeatureResponse feature: result.getFeatures()) {
         PackageFeatureModel featureModel = packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(feature.getSecureId());

         feature.setDesc(featureModel.getDescription());
      }

      if (model != null) {
         response.setSuccess(result);
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createPackage(CreatePackageRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         PackageModel model = packageMapper.createRequestToModel(request);
         packageRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updatePackage(String secureId, UpdatePackageRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      PackageModel model = packageRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      try {
         packageRepository.save(packageMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

}
