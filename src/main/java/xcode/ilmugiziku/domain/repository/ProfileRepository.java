package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ProfileModel;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, String> {

    @Query(value = "SELECT * FROM t_profile" +
            " WHERE user_secure_id = :user AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<ProfileModel> getProfileBySecureId(String user);

    @Query(value = "SELECT * FROM t_profile" +
            " WHERE email = :email AND deleted_at IS NULL" +
            " LIMIT 1", nativeQuery = true)
    Optional<ProfileModel> getProfileByEmail(String email);
}
