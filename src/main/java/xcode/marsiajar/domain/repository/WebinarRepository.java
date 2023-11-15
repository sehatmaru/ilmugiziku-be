package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.WebinarModel;

import java.util.List;

@Repository
public interface WebinarRepository extends JpaRepository<WebinarModel, String> {

   WebinarModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<WebinarModel> findAllByDeletedAtIsNullOrderByUpdatedAtDesc();
}
