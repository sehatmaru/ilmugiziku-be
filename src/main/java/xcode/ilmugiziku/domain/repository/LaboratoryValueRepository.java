package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.LaboratoryValueModel;

import java.util.List;

@Repository
public interface LaboratoryValueRepository extends JpaRepository<LaboratoryValueModel, String> {

   LaboratoryValueModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<LaboratoryValueModel> findByDeletedAtIsNull();
}
