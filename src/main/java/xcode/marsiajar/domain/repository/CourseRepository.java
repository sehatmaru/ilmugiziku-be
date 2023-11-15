package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.CourseModel;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, String> {
   CourseModel findBySecureIdAndDeletedAtIsNull(String secureId);
   CourseModel findByCategoryAndDeletedAtIsNull(String categorySecureId);
   List<CourseModel> findByDeletedAtIsNull();
}
