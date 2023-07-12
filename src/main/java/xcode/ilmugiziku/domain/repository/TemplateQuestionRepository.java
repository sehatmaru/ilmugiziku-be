package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.TemplateQuestionRelModel;

import java.util.List;

@Repository
public interface TemplateQuestionRepository extends JpaRepository<TemplateQuestionRelModel, String> {

   void deleteAllByTemplate(String template);

   @Query(value = "SELECT * FROM t_template_question_rel" +
           " WHERE template_secure_id = :template", nativeQuery = true)
   List<TemplateQuestionRelModel> getTemplateQuestionByTemplate(String template);
}
