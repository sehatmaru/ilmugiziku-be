package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.CourseBenefitRelModel;

import java.util.List;

@Repository
public interface CourseBenefitRepository extends JpaRepository<CourseBenefitRelModel, String> {
   List<CourseBenefitRelModel> findByDeletedIsFalse();
}
