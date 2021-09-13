package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.InstituteModel;

import java.util.List;

@Repository
public interface InstituteRepository extends JpaRepository<InstituteModel, String> {

   InstituteModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<InstituteModel> findAllByDeletedAtIsNull();
}
