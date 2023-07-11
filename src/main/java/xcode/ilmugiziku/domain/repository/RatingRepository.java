package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.RatingModel;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingModel, String> {

    @Query(value = "SELECT * FROM t_rating" +
            " WHERE course_secure_id = :course AND user_secure_id = :user" +
            " LIMIT 1", nativeQuery = true)
    RatingModel getCourseRating(String course, String user);

    @Query(value = "SELECT * FROM t_rating" +
            " WHERE course_secure_id = :course", nativeQuery = true)
    List<RatingModel> getAllCourseRating(String course);

    @Query(value = "SELECT * FROM t_rating" +
            " WHERE webinar_secure_id = :webinar", nativeQuery = true)
    List<RatingModel> getAllWebinarRating(String webinar);

    @Query(value = "SELECT * FROM t_rating" +
            " WHERE webinar_secure_id = :webinar AND user_secure_id = :user" +
            " LIMIT 1", nativeQuery = true)
    RatingModel getWebinarRating(String webinar, String user);

}
