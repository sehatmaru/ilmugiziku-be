package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.RoleEnum;
import xcode.ilmugiziku.domain.model.UserModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

   UserModel findBySecureId(String secureId);

   UserModel findBySecureIdAndDeletedAtIsNull(String secureId);

   UserModel findByEmailAndDeletedAtIsNull(String email);

   List<UserModel> findByRoleAndDeletedAtIsNull(RoleEnum role);

   UserModel findByEmailAndRole(String email, RoleEnum role);

   UserModel findByEmailAndPasswordAndDeletedAtIsNull(String email, String password);

   UserModel findByEmailAndPasswordAndRoleAndDeletedAtIsNull(String email, String password, RoleEnum role);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE username = :username AND active IS TRUE" +
           " AND deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   Optional<UserModel> getActiveUserByUsername(String username);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE secure_id = :secureId AND deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   UserModel getUserBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_user" +
           " WHERE secure_id = :secureId AND active IS TRUE" +
           " AND deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   Optional<UserModel> getActiveUserBySecureId(String secureId);

   @Query(value = "SELECT u.* FROM t_user u" +
           " LEFT JOIN t_group_member m ON u.secure_id = m.member_secure_id" +
           " WHERE m.group_secure_id <> :group AND u.secure_id <> :user" +
           " AND u.deleted_at IS NULL" +
           " AND u.active IS TRUE" +
           " OR leave_at IS NOT NULL", nativeQuery = true)
   List<UserModel> getGroupNonMemberList(String group, String user);
}
