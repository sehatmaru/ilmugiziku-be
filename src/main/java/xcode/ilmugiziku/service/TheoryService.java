package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
import xcode.ilmugiziku.domain.repository.TheoryRepository;
import xcode.ilmugiziku.domain.request.*;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.presenter.SchedulePresenter;
import xcode.ilmugiziku.presenter.TheoryPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.UKOM;

@Service
public class TheoryService implements TheoryPresenter {

   final TheoryRepository theoryRepository;

   public TheoryService(TheoryRepository theoryRepository) {
      this.theoryRepository = theoryRepository;
   }

   @Override
   public BaseResponse<List<TheoryResponse>> getTheoryList(int theoryType) {
      BaseResponse<List<TheoryResponse>> response = new BaseResponse<>();
      List<TheoryResponse> responses = new ArrayList<>();

      if (theoryType == UKOM || theoryType == SKB_GIZI) {
         List<TheoryModel> models = new ArrayList<>();

         try {
            models = theoryRepository.findByTheoryTypeAndDeletedAtIsNull(theoryType);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (models != null) {
            for (TheoryModel model : models) {
               TheoryResponse resp = new TheoryResponse();
               resp.setSecureId(model.getSecureId());
               resp.setCompetence(model.getCompetence());
               resp.setTheoryType(model.getTheoryType());
               resp.setUri(model.getUri());

               responses.add(resp);
            }

            response.setStatusCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);
            response.setResult(responses);
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createTheory(CreateTheoryRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (request.validate()) {
         String tempSecureId = generateSecureId();

         TheoryModel model = new TheoryModel();
         model.setSecureId(tempSecureId);
         model.setCompetence(request.getCompetence());
         model.setUri(request.getUri());
         model.setTheoryType(request.getTheoryType());
         model.setCreatedAt(new Date());

         try {
            theoryRepository.save(model);

            createResponse.setSecureId(tempSecureId);

            response.setSuccess(createResponse);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateTheory(UpdateTheoryRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TheoryModel model = new TheoryModel();

      try {
         model = theoryRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());
      } catch (Exception e) {
         response.setFailed(e.toString());
      }

      model.setCompetence(request.getCompetence());
      model.setUri(request.getUri());
      model.setTheoryType(request.getTheoryType());
      model.setUpdatedAt(new Date());

      try {
         theoryRepository.save(model);

         response.setSuccess(true);
      } catch (Exception e){
         response.setFailed(e.toString());
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteTheory(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
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

      return response;
   }
}
