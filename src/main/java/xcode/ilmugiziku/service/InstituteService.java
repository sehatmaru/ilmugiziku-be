package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.InstituteModel;
import xcode.ilmugiziku.domain.repository.InstituteRepository;
import xcode.ilmugiziku.domain.request.institution.CreateInstituteRequest;
import xcode.ilmugiziku.domain.request.institution.UpdateInstituteRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.InstituteResponse;
import xcode.ilmugiziku.mapper.InstituteMapper;
import xcode.ilmugiziku.presenter.InstitutePresenter;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class InstituteService implements InstitutePresenter {

   private final AuthTokenService authTokenService;

   private final InstituteRepository instituteRepository;

   private final InstituteMapper instituteMapper = new InstituteMapper();

   public InstituteService(AuthTokenService authTokenService, InstituteRepository instituteRepository) {
      this.authTokenService = authTokenService;
      this.instituteRepository = instituteRepository;
   }

   @Override
   public BaseResponse<List<InstituteResponse>> getInstitutionList(String token) {
      BaseResponse<List<InstituteResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         response.setSuccess(instituteMapper.modelsToResponses(instituteRepository.findAllByDeletedAtIsNull()));
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createInstitute(String token, CreateInstituteRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               InstituteModel model = instituteMapper.createRequestToModel(request);
               instituteRepository.save(model);

               createResponse.setSecureId(model.getSecureId());

               response.setSuccess(createResponse);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateInstitute(String token, UpdateInstituteRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         InstituteModel model = instituteRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());

         try {
            instituteRepository.save(instituteMapper.updateRequestToModel(model, request));

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
   public BaseResponse<Boolean> deleteInstitute(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         InstituteModel model = instituteRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               instituteRepository.save(model);

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
}
