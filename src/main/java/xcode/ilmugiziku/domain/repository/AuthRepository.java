package xcode.ilmugiziku.domain.repository;

import xcode.ilmugiziku.domain.model.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, String> {

   AuthModel findBySecureIdAndDeletedAtIsNull(String secureId);

   AuthModel findByEmailAndDeletedAtIsNull(String email);

   AuthModel findByEmailAndRole(String email, int role);

   AuthModel findByEmailAndPasswordAndDeletedAtIsNull(String email, String password);

   AuthModel findByEmailAndPasswordAndRoleAndDeletedAtIsNull(String email, String password, int role);
}
