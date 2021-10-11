package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;

public interface RatingPresenter {
   BaseResponse<Double> submitRating(String token, String lessonSecureId, SubmitRatingRequest request);
}
