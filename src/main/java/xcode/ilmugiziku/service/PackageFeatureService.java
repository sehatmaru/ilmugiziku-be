package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.PackageFeatureModel;
import xcode.ilmugiziku.domain.repository.PackageFeatureRepository;
import xcode.ilmugiziku.domain.request.packagefeature.CreatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.packagefeature.UpdatePackageFeatureRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PackageFeatureResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.PackageFeatureMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class PackageFeatureService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private PackageFeatureRepository packageFeatureRepository;

   private final PackageFeatureMapper packageFeatureMapper = new PackageFeatureMapper();

   public BaseResponse<List<PackageFeatureResponse>> getPackageFeatureList(String token) {
      BaseResponse<List<PackageFeatureResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         List<PackageFeatureModel> models = packageFeatureRepository.findByDeletedAtIsNull();

         response.setSuccess(packageFeatureMapper.modelsToResponses(models));
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

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
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updatePackageFeature(String token, String secureId, UpdatePackageFeatureRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         PackageFeatureModel model = packageFeatureRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         try {
            packageFeatureRepository.save(packageFeatureMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

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
               throw new AppException(e.toString());
            }
         } else {
            throw new AppException(NOT_FOUND_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }
}
