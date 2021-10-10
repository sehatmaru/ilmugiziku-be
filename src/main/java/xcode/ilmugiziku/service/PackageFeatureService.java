package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.PackageFeatureModel;
import xcode.ilmugiziku.domain.repository.PackageFeatureRepository;
import xcode.ilmugiziku.domain.request.packagefeature.CreatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.packagefeature.UpdatePackageFeatureRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PackageFeatureResponse;
import xcode.ilmugiziku.mapper.PackageFeatureMapper;
import xcode.ilmugiziku.presenter.PackageFeaturePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class PackageFeatureService implements PackageFeaturePresenter {

   private final AuthTokenService authTokenService;

   private final PackageFeatureRepository packageFeatureRepository;

   private final PackageFeatureMapper packageFeatureMapper = new PackageFeatureMapper();

   public PackageFeatureService(AuthTokenService authTokenService, PackageFeatureRepository packageFeatureRepository) {
      this.authTokenService = authTokenService;
      this.packageFeatureRepository = packageFeatureRepository;
   }

   @Override
   public BaseResponse<List<PackageFeatureResponse>> getPackageFeatureList(String token) {
      BaseResponse<List<PackageFeatureResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         List<PackageFeatureModel> models = new ArrayList<>();

         try {
            models = packageFeatureRepository.findByDeletedAtIsNull();
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         response.setSuccess(packageFeatureMapper.modelsToResponses(models));
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createPackageFeature(String token, CreatePackageFeatureRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         try {
            PackageFeatureModel model = packageFeatureMapper.createRequestToModel(request);
            packageFeatureRepository.save(model);

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

   @Override
   public BaseResponse<Boolean> updatePackageFeature(String token, String secureId, UpdatePackageFeatureRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageFeatureModel model = packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         try {
            packageFeatureRepository.save(packageFeatureMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deletePackageFeature(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageFeatureModel model = packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               packageFeatureRepository.save(model);

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

   public PackageFeatureModel getPackageFeatureBySecureId(String secureId) {
      return packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(secureId);
   }
}
