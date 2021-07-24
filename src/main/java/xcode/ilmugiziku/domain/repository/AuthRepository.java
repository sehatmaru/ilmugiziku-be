package xcode.ilmugiziku.domain.repository;

import xcode.ilmugiziku.domain.model.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, String> {

   AuthModel findBySecureId(String secureId);

   AuthModel findBySecureIdAndDeletedAtIsNull(String secureId);

   AuthModel findByEmailAndDeletedAtIsNull(String email);

   List<AuthModel> findByRoleAndDeletedAtIsNull(int role);

   AuthModel findByEmailAndRole(String email, int role);

   AuthModel findByEmailAndPasswordAndDeletedAtIsNull(String email, String password);

   AuthModel findByEmailAndPasswordAndRoleAndDeletedAtIsNull(String email, String password, int role);
}
