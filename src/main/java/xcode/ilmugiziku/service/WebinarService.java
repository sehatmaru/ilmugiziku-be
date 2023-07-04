package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.WebinarModel;
import xcode.ilmugiziku.domain.repository.WebinarRepository;
import xcode.ilmugiziku.domain.request.webinar.CreateWebinarRequest;
import xcode.ilmugiziku.domain.request.webinar.UpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.WebinarMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;

@Service
public class WebinarService {

   @Autowired private WebinarRepository webinarRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();

   public BaseResponse<List<WebinarResponse>> getWebinarList(int bimbelType) {
      BaseResponse<List<WebinarResponse>> response = new BaseResponse<>();

      if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
         List<WebinarModel> models = webinarRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);

         response.setSuccess(webinarMapper.modelsToResponses(models));
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createWebinar(CreateWebinarRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         WebinarModel model = webinarMapper.createRequestToModel(request);
         webinarRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateWebinar(String secureId, UpdateWebinarRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         WebinarModel model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         webinarRepository.save(webinarMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteWebinar(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      WebinarModel model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            webinarRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

}
