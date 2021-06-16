package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.AnswerModel;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerModel, String> {

   AnswerModel findById(int id);

   AnswerModel findBySecureId(String secureId);

   AnswerModel findByQuestionSecureIdAndValue(String questionSecureId, boolean value);

   List<AnswerModel> findAllByQuestionSecureId(String questionSecureId);
}
