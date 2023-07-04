package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.RatingModel;
import xcode.ilmugiziku.domain.repository.RatingRepository;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.exception.AppException;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.EXIST_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class RatingService  {

   @Autowired private LessonService lessonService;
   @Autowired private RatingRepository ratingRepository;

   public BaseResponse<Double> submitRating(String lessonSecureId, SubmitRatingRequest request) {
      BaseResponse<Double> response = new BaseResponse<>();

      if (!isRatedByUserSecureId(CurrentUser.get().getUserSecureId(), lessonSecureId)) {
         RatingModel ratingModel = new RatingModel();
         ratingModel.setSecureId(generateSecureId());
         ratingModel.setUserSecureId(CurrentUser.get().getUserSecureId());
         ratingModel.setLessonSecureId(lessonSecureId);
         ratingModel.setRating(request.getRating());
         ratingModel.setCreatedAt(new Date());

         ratingRepository.save(ratingModel);

         response.setSuccess(calculateRating(lessonSecureId));
      } else {
         throw new AppException(EXIST_MESSAGE);
      }

      return response;
   }

   public boolean isRatedByUserSecureId(String userSecureId, String lessonSecureId) {
      return ratingRepository.findByUserSecureIdAndLessonSecureIdAndDeletedAtIsNull(userSecureId, lessonSecureId) != null;
   }

   private double calculateRating(String lessonSecureId) {
      double result;

      double totalRating = 0.0;
      List<RatingModel> ratings = ratingRepository.findAllByLessonSecureIdAndDeletedAtIsNull(lessonSecureId);

      for (RatingModel rating: ratings) {
         totalRating += rating.getRating();
      }

      result = totalRating/ratings.size();

      lessonService.saveLessonRating(lessonSecureId, result);

      return result;
   }
}
