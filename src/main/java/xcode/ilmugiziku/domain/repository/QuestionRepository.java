package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;
import xcode.ilmugiziku.domain.model.QuestionModel;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, String> {

   QuestionModel findById(int id);

   QuestionModel findBySecureId(String secureId);

   List<QuestionModel> findByQuestionTypeAndDeletedAtIsNull(QuestionTypeEnum questionType);

   List<QuestionModel> findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType, String secureId);
}
