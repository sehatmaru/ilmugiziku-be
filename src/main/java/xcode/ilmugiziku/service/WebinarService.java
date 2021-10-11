package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.WebinarModel;
import xcode.ilmugiziku.domain.repository.WebinarRepository;
import xcode.ilmugiziku.domain.request.webinar.CreateWebinarRequest;
import xcode.ilmugiziku.domain.request.webinar.UpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;
import xcode.ilmugiziku.mapper.WebinarMapper;
import xcode.ilmugiziku.presenter.WebinarPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;

@Service
public class WebinarService implements WebinarPresenter {

   private final AuthTokenService authTokenService;

   private final WebinarRepository webinarRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();

   public WebinarService(AuthTokenService authTokenService, WebinarRepository webinarRepository) {
      this.authTokenService = authTokenService;
      this.webinarRepository = webinarRepository;
   }

   @Override
   public BaseResponse<List<WebinarResponse>> getWebinarList(String token, int bimbelType) {
      BaseResponse<List<WebinarResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
            List<WebinarModel> models = new ArrayList<>();

            try {
               models = webinarRepository.findByBimbelTypeAndDeletedAtIsNull(bimbelType);
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            response.setSuccess(webinarMapper.modelsToResponses(models));
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createWebinar(String token, CreateWebinarRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               WebinarModel model = webinarMapper.createRequestToModel(request);
               webinarRepository.save(model);

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
   public BaseResponse<Boolean> updateWebinar(String token, String secureId, UpdateWebinarRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         WebinarModel model = new WebinarModel();

         try {
            model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         try {
            webinarRepository.save(webinarMapper.updateRequestToModel(model, request));

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
   public BaseResponse<Boolean> deleteWebinar(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         WebinarModel model = new WebinarModel();

         try {
            model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               webinarRepository.save(model);

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
