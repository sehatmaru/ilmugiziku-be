package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.model.CourseModel;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, String> {
   CourseModel findBySecureIdAndDeletedAtIsNull(String secureId);
   CourseModel findByCourseTypeAndDeletedAtIsNull(CourseTypeEnum type);
   List<CourseModel> findByDeletedAtIsNull();
}
