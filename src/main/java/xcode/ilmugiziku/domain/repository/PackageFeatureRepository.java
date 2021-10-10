package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.PackageFeatureModel;

import java.util.List;

@Repository
public interface PackageFeatureRepository extends JpaRepository<PackageFeatureModel, String> {

   PackageFeatureModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<PackageFeatureModel> findByDeletedAtIsNull();
}
