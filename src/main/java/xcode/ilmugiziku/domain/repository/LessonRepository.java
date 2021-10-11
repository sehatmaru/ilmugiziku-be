package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.LessonModel;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, String> {

   LessonModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<LessonModel> findByBimbelTypeAndDeletedAtIsNull(int type);
}
