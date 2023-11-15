package xcode.marsiajar.service;

import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.model.RatingModel;

import java.util.List;

@Service
public class RatingService  {

   /**
    * recalculate webinar/course rating
    * @param ratings = list of rating
    * @return final rating
    */
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
