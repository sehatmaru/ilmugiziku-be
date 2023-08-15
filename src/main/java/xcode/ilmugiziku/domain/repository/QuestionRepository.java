package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.QuestionModel;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, String> {

   QuestionModel findBySecureId(String secureId);

   @Query(value = "SELECT * FROM t_question " +
           " ORDER BY updated_at DESC", nativeQuery = true)
   List<QuestionModel> findAllQuestions();

}
