package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.QuestionModel;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, String> {

   QuestionModel findBySecureId(String secureId);

}
