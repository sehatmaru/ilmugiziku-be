package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.BenefitModel;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitModel, String> {

   BenefitModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<BenefitModel> findByDeletedAtIsNull();

   @Query(value = "SELECT * FROM t_benefit " +
           " WHERE secure_id = :benefit AND deleted_at IS NULL", nativeQuery = true)
   List<BenefitModel> getCourseBenefits(String benefit);
}
