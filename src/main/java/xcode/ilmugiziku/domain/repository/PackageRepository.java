package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.PackageModel;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageModel, String> {

   PackageModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<PackageModel> findByDeletedAtIsNull();
}
