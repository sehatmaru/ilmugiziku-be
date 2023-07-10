package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.UserCourseRelModel;
import xcode.ilmugiziku.domain.model.UserWebinarRelModel;

import java.util.List;

@Repository
public interface UserWebinarRepository extends JpaRepository<UserWebinarRelModel, String> {

   @Query(value = "SELECT * FROM t_user_webinar_rel" +
           " WHERE user_secure_id = :user AND webinar_secure_id = :webinar" +
           " AND active = TRUE" +
           " AND deleted = FALSE" +
           " LIMIT 1", nativeQuery = true)
   UserWebinarRelModel getActiveUserWebinar(String user, String webinar);

   @Query(value = "SELECT * FROM t_user_course_rel" +
           " WHERE active = TRUE AND deleted = FALSE", nativeQuery = true)
   List<UserCourseRelModel> getAllActiveCourse();
}
