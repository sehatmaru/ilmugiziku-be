package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.BenefitModel;
import xcode.ilmugiziku.domain.repository.BenefitRepository;
import xcode.ilmugiziku.domain.request.benefit.CreateUpdateBenefitRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.benefit.BenefitResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.BenefitMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class BenefitService {

   @Autowired private BenefitRepository benefitRepository;

   private final BenefitMapper packageFeatureMapper = new BenefitMapper();

   public BaseResponse<List<BenefitResponse>> getBenefitList() {
      BaseResponse<List<BenefitResponse>> response = new BaseResponse<>();

      List<BenefitModel> models = benefitRepository.findByDeletedAtIsNull();

      response.setSuccess(packageFeatureMapper.modelsToResponses(models));

      return response;
   }

   public BaseResponse<CreateBaseResponse> createBenefit(CreateUpdateBenefitRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         BenefitModel model = packageFeatureMapper.createRequestToModel(request);
         benefitRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateBenefit(String secureId, CreateUpdateBenefitRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      BenefitModel model = benefitRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      try {
         benefitRepository.save(packageFeatureMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteBenefit(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      BenefitModel model = benefitRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            benefitRepository.save(model);

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
