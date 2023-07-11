package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.UserExamRelModel;

@Repository
public interface UserExamRepository extends JpaRepository<UserExamRelModel, String> {

   @Query(value = "SELECT * FROM t_user_exam_rel" +
           " WHERE user_secure_id = :user AND exam_secure_id = :exam" +
           " AND deleted = FALSE" +
           " LIMIT 1", nativeQuery = true)
   UserExamRelModel getUserExam(String user, String exam);
}