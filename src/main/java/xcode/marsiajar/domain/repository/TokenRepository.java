package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.TokenModel;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, String> {
    TokenModel findByToken(String token);

    TokenModel findByTokenAndTemporaryIsTrue(String token);

    @Query(value = "SELECT * FROM t_token" +
            " WHERE user_secure_id = :userId AND temporary = FALSE" +
            " LIMIT 1", nativeQuery = true)
    TokenModel getTokenByUser(String userId);

    @Query(value = "SELECT * FROM t_token" +
            " WHERE token = :token" +
            " LIMIT 1", nativeQuery = true)
    TokenModel getToken(String token);
}
