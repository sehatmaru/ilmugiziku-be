package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.RatingModel;
import xcode.ilmugiziku.domain.repository.RatingRepository;

import java.util.List;

@Service
public class RatingService  {

   @Autowired private RatingRepository ratingRepository;

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
