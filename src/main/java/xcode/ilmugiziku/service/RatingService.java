package xcode.ilmugiziku.service;

import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.RatingModel;
import xcode.ilmugiziku.domain.repository.RatingRepository;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.presenter.RatingPresenter;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class RatingService implements RatingPresenter {

   private final AuthTokenService authTokenService;
   private final LessonService lessonService;

   private final RatingRepository ratingRepository;

   public RatingService(AuthTokenService authTokenService,
                        RatingRepository ratingRepository,
                        @NonNull @Lazy LessonService lessonService) {
      this.authTokenService = authTokenService;
      this.ratingRepository = ratingRepository;
      this.lessonService = lessonService;
   }

   @Override
   public BaseResponse<Double> submitRating(String token, String lessonSecureId, SubmitRatingRequest request) {
      BaseResponse<Double> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

         if (!isRatedByAuthSecureId(authTokenModel.getAuthSecureId(), lessonSecureId)) {
            RatingModel ratingModel = new RatingModel();
            ratingModel.setSecureId(generateSecureId());
            ratingModel.setAuthSecureId(authTokenModel.getAuthSecureId());
            ratingModel.setLessonSecureId(lessonSecureId);
            ratingModel.setRating(request.getRating());
            ratingModel.setCreatedAt(new Date());

            ratingRepository.save(ratingModel);

            response.setSuccess(calculateRating(lessonSecureId));
         } else {
            response.setExistData("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public boolean isRatedByAuthSecureId(String authSecureId, String lessonSecureId) {
      return ratingRepository.findByAuthSecureIdAndLessonSecureIdAndDeletedAtIsNull(authSecureId, lessonSecureId) != null;
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
