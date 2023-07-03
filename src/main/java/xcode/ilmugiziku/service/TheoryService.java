package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.TheoryModel;
import xcode.ilmugiziku.domain.repository.TheoryRepository;
import xcode.ilmugiziku.domain.request.theory.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.theory.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TheoryResponse;
import xcode.ilmugiziku.mapper.TheoryMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.UKOM;

@Service
public class TheoryService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private TheoryRepository theoryRepository;

   private final TheoryMapper theoryMapper = new TheoryMapper();

   public BaseResponse<List<TheoryResponse>> getTheoryList(String token, int theoryType) {
      BaseResponse<List<TheoryResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (theoryType == UKOM || theoryType == SKB_GIZI) {
            List<TheoryModel> models = new ArrayList<>();

            try {
               models = theoryRepository.findByTheoryTypeAndDeletedAtIsNull(theoryType);
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            response.setSuccess(theoryMapper.modelsToResponses(models));
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createTheory(String token, CreateTheoryRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               TheoryModel model = theoryMapper.createRequestToModel(request);
               theoryRepository.save(model);

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

   public BaseResponse<Boolean> updateTheory(String token, UpdateTheoryRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TheoryModel model = new TheoryModel();

         try {
            model = theoryRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         try {
            theoryRepository.save(theoryMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTheory(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TheoryModel model = new TheoryModel();

         try {
            model = theoryRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               theoryRepository.save(model);

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
