package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.RatingModel;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingModel, String> {

   RatingModel findBySecureIdAndDeletedAtIsNull(String secureId);

   RatingModel findByAuthSecureIdAndLessonSecureIdAndDeletedAtIsNull(String auth, String lesson);

   List<RatingModel> findAllByLessonSecureIdAndDeletedAtIsNull(String lesson);
}
