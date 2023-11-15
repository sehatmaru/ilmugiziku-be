package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.UserWebinarRelModel;

import java.util.List;

@Repository
public interface UserWebinarRepository extends JpaRepository<UserWebinarRelModel, String> {

   @Query(value = "SELECT * FROM t_user_webinar_rel" +
           " WHERE user_secure_id = :user AND webinar_secure_id = :webinar" +
           " AND active = TRUE" +
           " AND deleted = FALSE" +
           " LIMIT 1", nativeQuery = true)
   UserWebinarRelModel getActiveUserWebinar(String user, String webinar);

   @Query(value = "SELECT * FROM t_user_webinar_rel" +
           " WHERE secure_id = :secureId" +
           " AND deleted = FALSE", nativeQuery = true)
   UserWebinarRelModel getActiveUserWebinarBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_user_webinar_rel" +
           " WHERE secure_id = :secureId", nativeQuery = true)
   UserWebinarRelModel getUserWebinarBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_user_webinar_rel uw" +
           " LEFT JOIN t_invoice i ON i.user_webinar_secure_id = uw.secure_id" +
           " WHERE uw.user_secure_id = :user AND uw.webinar_secure_id = :webinar" +
           " AND i.invoice_status IN ('PAID')" +
           " LIMIT 1", nativeQuery = true)
   UserWebinarRelModel getPaidUserWebinar(String user, String webinar);

   @Query(value = "SELECT * FROM t_user_webinar_rel uw" +
           " LEFT JOIN t_webinar w ON w.secure_id = uw.webinar_secure_id" +
           " WHERE uw.active = TRUE AND w.available = TRUE" +
           " AND uw.deleted = FALSE", nativeQuery = true)
   List<UserWebinarRelModel> getAllUpcomingWebinar();
}
