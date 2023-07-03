package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.PackageFeatureModel;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.repository.PackageRepository;
import xcode.ilmugiziku.domain.request.pack.CreatePackageRequest;
import xcode.ilmugiziku.domain.request.pack.UpdatePackageRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.pack.PackageFeatureResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;
import xcode.ilmugiziku.mapper.PackageMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class PackageService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthService authService;
   @Autowired private PackageFeatureService packageFeatureService;
   @Autowired private PackageRepository packageRepository;

   private final PackageMapper packageMapper = new PackageMapper();

   public BaseResponse<List<PackageResponse>> getPackageList(String token) {
      BaseResponse<List<PackageResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getAuthBySecureId(authTokenModel.getAuthSecureId());
         List<PackageModel> models = packageRepository.findByDeletedAtIsNull();
         List<PackageResponse> responses = packageMapper.modelsToResponses(models);

         for (PackageResponse resp : responses) {
            resp.setOpen(!authModel.isPaidPackage(resp.getPackageType()));

            for (PackageFeatureResponse feature: resp.getFeatures()) {
               PackageFeatureModel model = packageFeatureService.getPackageFeatureBySecureId(feature.getSecureId());

               feature.setDesc(model.getDescription());
            }
         }

         response.setSuccess(responses);
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<PackageResponse> getPackage(String token, String secureId) {
      BaseResponse<PackageResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageModel model = packageRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         PackageResponse result = packageMapper.modelToResponse(model);

         for (PackageFeatureResponse feature: result.getFeatures()) {
            PackageFeatureModel featureModel = packageFeatureService.getPackageFeatureBySecureId(feature.getSecureId());

            feature.setDesc(featureModel.getDescription());
         }

         if (model != null) {
            response.setSuccess(result);
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createPackage(String token, CreatePackageRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         try {
            PackageModel model = packageMapper.createRequestToModel(request);
            packageRepository.save(model);

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

   public BaseResponse<Boolean> updatePackage(String token, String secureId, UpdatePackageRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageModel model = packageRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         try {
            packageRepository.save(packageMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deletePackage(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageModel model = packageRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               packageRepository.save(model);

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

   public PackageModel getPackageByType(int type) {
      return packageRepository.findByPackageTypeAndDeletedAtIsNull(type);
   }
}
