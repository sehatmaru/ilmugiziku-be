package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.enums.RoleEnum;
import xcode.marsiajar.domain.model.UserModel;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

   UserModel findBySecureId(String secureId);

   UserModel findBySecureIdAndDeletedAtIsNull(String secureId);

   UserModel findByEmailAndDeletedAtIsNull(String email);

   List<UserModel> findByRoleAndDeletedAtIsNullOrderByCreatedAtDesc(RoleEnum role);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE email = :email AND role = 'ADMIN'" +
           " AND deleted_at IS NULL AND active = TRUE" +
           " LIMIT 1", nativeQuery = true)
   UserModel getActiveAdmin(String email);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE email = :email AND role = 'CONSUMER'" +
           " AND deleted_at IS NULL AND active = TRUE" +
           " LIMIT 1", nativeQuery = true)
   UserModel getActiveConsumerByEmail(String email);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE secure_id = :secureId AND deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   UserModel getUserBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE secure_id = :secureId AND active IS TRUE" +
           " AND deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   UserModel getActiveUserBySecureId(String secureId);

   @Query(value = "SELECT u.* FROM t_user u" +
           " LEFT JOIN t_user_course_rel uc ON uc.user_secure_id = u.secure_id" +
           " WHERE uc.secure_id = :userCourse" +
           " AND u.deleted_at IS NULL" +
           " AND u.active IS TRUE" +
           " AND uc.deleted IS FALSE", nativeQuery = true)
   UserModel getUserByUserCourse(String userCourse);

}
