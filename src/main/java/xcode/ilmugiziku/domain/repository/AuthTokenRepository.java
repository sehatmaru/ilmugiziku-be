package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.AuthTokenModel;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenModel, String> {

   AuthTokenModel findByToken(String token);

   AuthTokenModel findByAuthSecureId(String authSecureId);

   boolean existsByToken(String token);

   boolean existsByAuthSecureId(String authSecureId);
}
