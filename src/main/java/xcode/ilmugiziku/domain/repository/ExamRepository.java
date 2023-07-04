package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ExamModel;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String> {

   ExamModel findBySecureId(String secureId);

   ExamModel findByScheduleSecureIdAndUserSecureIdAndQuestionTypeAndQuestionSubType(String scheduleSecureId, String userSecureId, int questionType, int questionSubType);

   List<ExamModel> findByScheduleSecureIdAndUserSecureId(String scheduleSecureId, String userSecureId);

   List<ExamModel> findByScheduleSecureIdAndUserSecureIdAndQuestionType(String scheduleSecureId, String userSecureId, int questionType);

   List<ExamModel> findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(String scheduleSecureId, int questionType, int questionSubType);

   List<ExamModel> findAllByUserSecureId(String userSecureId);
}
