package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.RatingModel;
import xcode.ilmugiziku.domain.repository.RatingRepository;

import java.util.List;

@Service
public class RatingService  {

   @Autowired private RatingRepository ratingRepository;

   // TODO: 11/07/23
//   public BaseResponse<Double> submitRating(String lessonSecureId, SubmitRatingRequest request) {
//      BaseResponse<Double> response = new BaseResponse<>();
//
//      if (!isRatedByUserSecureId(CurrentUser.get().getUserSecureId(), lessonSecureId)) {
//         RatingModel ratingModel = new RatingModel();
//         ratingModel.setSecureId(generateSecureId());
//         ratingModel.setUserSecureId(CurrentUser.get().getUserSecureId());
//         ratingModel.setLessonSecureId(lessonSecureId);
//         ratingModel.setRating(request.getRating());
//         ratingModel.setCreatedAt(new Date());
//
//         ratingRepository.save(ratingModel);
//
//         response.setSuccess(calculateRating(lessonSecureId));
//      } else {
//         throw new AppException(EXIST_MESSAGE);
//      }
//
//      return response;
//   }

   // TODO: 11/07/23
//   public boolean isRatedByUserSecureId(String userSecureId, String lessonSecureId) {
//      return ratingRepository.findByUserSecureIdAndLessonSecureIdAndDeletedAtIsNull(userSecureId, lessonSecureId) != null;
//   }

   public double calculateRatings(List<RatingModel> ratings) {
      double result;

      double totalRating = 0.0;

      for (RatingModel rating: ratings) {
         totalRating += rating.getRating();
      }

      result = totalRating/ratings.size();

      return result;
   }
}
