package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ExamModel;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String> {

   ExamModel findBySecureId(String secureId);

   ExamModel findByScheduleSecureIdAndAuthSecureIdAndQuestionTypeAndQuestionSubType(String scheduleSecureId, String authSecureId, int questionType, int questionSubType);

   List<ExamModel> findByScheduleSecureIdAndAuthSecureId(String scheduleSecureId, String authSecureId);

   List<ExamModel> findByScheduleSecureIdAndAuthSecureIdAndQuestionType(String scheduleSecureId, String authSecureId, int questionType);

   List<ExamModel> findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(String scheduleSecureId, int questionType, int questionSubType);

   List<ExamModel> findAllByAuthSecureId(String authSecureId);
}
