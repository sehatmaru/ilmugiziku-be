package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.CourseBenefitRelModel;

import java.util.List;

@Repository
public interface CourseBenefitRepository extends JpaRepository<CourseBenefitRelModel, String> {
   List<CourseBenefitRelModel> findByDeletedIsFalse();

   @Query(value = "SELECT * FROM t_course_benefit_rel" +
           " WHERE course_secure_id = :course AND deleted = FALSE", nativeQuery = true)
   List<CourseBenefitRelModel> getCourseBenefits(String course);
}
