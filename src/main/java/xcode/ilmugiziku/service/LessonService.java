package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.model.LessonModel;
import xcode.ilmugiziku.domain.repository.LessonRepository;
import xcode.ilmugiziku.domain.request.lesson.CreateLessonRequest;
import xcode.ilmugiziku.domain.request.lesson.UpdateLessonRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LessonResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.LessonMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.CourseTypeEnum.SKB;
import static xcode.ilmugiziku.domain.enums.CourseTypeEnum.UKOM;
import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.PARAMS_ERROR_MESSAGE;

@Service
public class LessonService {

   @Autowired private RatingService ratingService;
   @Autowired private LessonRepository lessonRepository;

   private final LessonMapper lessonMapper = new LessonMapper();

   public BaseResponse<List<LessonResponse>> getLessonList(CourseTypeEnum courseType) {
      BaseResponse<List<LessonResponse>> response = new BaseResponse<>();

      if (courseType == UKOM || courseType == SKB) {
         List<LessonModel> models = lessonRepository.findAllByCourseTypeAndDeletedAtIsNull(courseType);
         List<LessonResponse> responses = lessonMapper.modelsToResponses(models);

         for (LessonResponse lesson : responses) {
            lesson.setRated(ratingService.isRatedByUserSecureId(CurrentUser.get().getUserSecureId(), lesson.getSecureId()));
         }

         response.setSuccess(responses);
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createLesson(CreateLessonRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         LessonModel model = lessonMapper.createRequestToModel(request);
         lessonRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateLesson(String secureId, UpdateLessonRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      try {
         lessonRepository.save(lessonMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteLesson(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            lessonRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<LessonResponse> getLesson(String secureId) {
      BaseResponse<LessonResponse> response = new BaseResponse<>();

      LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      LessonResponse result = lessonMapper.modelToResponse(model);
      result.setRated(ratingService.isRatedByUserSecureId(CurrentUser.get().getUserSecureId(), secureId));

      if (model != null) {
         response.setSuccess(result);
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public void saveLessonRating(String secureId, double rating) {
      LessonModel model = lessonRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      model.setRating(rating);

      lessonRepository.save(model);
   }
}
