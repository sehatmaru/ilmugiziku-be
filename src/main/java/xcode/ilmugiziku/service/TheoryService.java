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
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.TheoryMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
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
            List<TheoryModel> models = theoryRepository.findByTheoryTypeAndDeletedAtIsNull(theoryType);

            response.setSuccess(theoryMapper.modelsToResponses(models));
         } else {
            throw new AppException(PARAMS_ERROR_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
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
               throw new AppException(e.toString());
            }
         } else {
            throw new AppException(PARAMS_ERROR_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateTheory(String token, UpdateTheoryRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         try {
            TheoryModel model = theoryRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
            theoryRepository.save(theoryMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTheory(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TheoryModel model = theoryRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               theoryRepository.save(model);

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
