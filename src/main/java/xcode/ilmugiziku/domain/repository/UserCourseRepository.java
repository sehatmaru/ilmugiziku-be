package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.UserCourseRelModel;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseRelModel, String> {

   @Query(value = "SELECT * FROM t_user_course_rel" +
           " WHERE secure_id = :secureId" +
           " AND deleted = FALSE", nativeQuery = true)
   UserCourseRelModel getUserCourseBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_user_course_rel" +
           " WHERE user_secure_id = :user AND active = TRUE" +
           " AND deleted = FALSE", nativeQuery = true)
   List<UserCourseRelModel> getUserActiveCourse(String user);

   @Query(value = "SELECT * FROM t_user_course_rel" +
           " WHERE user_secure_id = :user AND course_secure_id = :course" +
           " AND deleted = FALSE" +
           " LIMIT 1", nativeQuery = true)
   UserCourseRelModel getUserCourse(String user, String course);

   @Query(value = "SELECT * FROM t_user_course_rel" +
           " WHERE active = TRUE AND deleted = FALSE", nativeQuery = true)
   List<UserCourseRelModel> getAllActiveCourse(String user);
}
