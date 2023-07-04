package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;
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

import static xcode.ilmugiziku.domain.enums.TheoryTypeEnum.SKB_GIZI;
import static xcode.ilmugiziku.domain.enums.TheoryTypeEnum.UKOM;
import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class TheoryService {

   @Autowired private TheoryRepository theoryRepository;

   private final TheoryMapper theoryMapper = new TheoryMapper();

   public BaseResponse<List<TheoryResponse>> getTheoryList(TheoryTypeEnum theoryType) {
      BaseResponse<List<TheoryResponse>> response = new BaseResponse<>();

      if (theoryType == UKOM || theoryType == SKB_GIZI) {
         List<TheoryModel> models = theoryRepository.findByTheoryTypeAndDeletedAtIsNull(theoryType);

         response.setSuccess(theoryMapper.modelsToResponses(models));
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createTheory(CreateTheoryRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         TheoryModel model = theoryMapper.createRequestToModel(request);
         theoryRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateTheory(UpdateTheoryRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         TheoryModel model = theoryRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
         theoryRepository.save(theoryMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTheory(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

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

      return response;
   }
}
