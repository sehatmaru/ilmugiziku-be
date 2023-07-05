package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.UserCourseRelModel;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseRelModel, String> {

   List<UserCourseRelModel> findByDeletedIsFalse();
}
