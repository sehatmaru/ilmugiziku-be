package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.LessonRepository;
import xcode.ilmugiziku.domain.request.lesson.CreateLessonRequest;
import xcode.ilmugiziku.domain.request.lesson.UpdateLessonRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LessonResponse;
import xcode.ilmugiziku.mapper.LessonMapper;
import xcode.ilmugiziku.presenter.LessonPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;

@Service
public class LessonService implements LessonPresenter {

   private final AuthTokenService authTokenService;
   private final RatingService ratingService;

   private final LessonRepository lessonRepository;

   private final LessonMapper lessonMapper = new LessonMapper();

   public LessonService(AuthTokenService authTokenService, LessonRepository lessonRepository, RatingService ratingService) {
      this.authTokenService = authTokenService;
      this.lessonRepository = lessonRepository;
      this.ratingService = ratingService;
   }

   @Override
   public BaseResponse<List<LessonResponse>> getLessonList(String token, int bimbelType) {
      BaseResponse<List<LessonResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
            AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
            List<LessonModel> models = new ArrayList<>();

            try {
               models = lessonRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            List<LessonResponse> responses = lessonMapper.modelsToResponses(models);

            for (LessonResponse lesson : responses) {
               lesson.setRated(ratingService.isRatedByAuthSecureId(authTokenModel.getAuthSecureId(), lesson.getSecureId()));
            }

            response.setSuccess(responses);
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createLesson(String token, CreateLessonRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               LessonModel model = lessonMapper.createRequestToModel(request);
               lessonRepository.save(model);

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
   public BaseResponse<Boolean> updateLesson(String token, String secureId, UpdateLessonRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         LessonModel model = new LessonModel();

         try {
            model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         try {
            lessonRepository.save(lessonMapper.updateRequestToModel(model, request));

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
   public BaseResponse<Boolean> deleteLesson(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         LessonModel model = new LessonModel();

         try {
            model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               lessonRepository.save(model);

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

   @Override
   public BaseResponse<LessonResponse> getLesson(String token, String secureId) {
      BaseResponse<LessonResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         LessonResponse result = lessonMapper.modelToResponse(model);

         if (model != null) {
            response.setSuccess(result);
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public void saveLessonRating(String secureId, double rating) {
      LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      model.setRating(rating);

      lessonRepository.save(model);
   }

   public List<LessonModel> getLessonByBimbelType(int type) {
      return lessonRepository.findAllByBimbelTypeAndDeletedAtIsNull(type);
   }
}
