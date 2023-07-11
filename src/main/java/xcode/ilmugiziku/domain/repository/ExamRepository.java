package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ExamModel;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String> {
   // TODO: 11/07/23  
//   ExamModel findByScheduleSecureIdAndUserSecureIdAndQuestionTypeAndQuestionSubType(String scheduleSecureId, String userSecureId, QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType);
//   List<ExamModel> findByScheduleSecureIdAndUserSecureIdAndQuestionType(String scheduleSecureId, String userSecureId, QuestionTypeEnum questionType);
//   List<ExamModel> findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(String scheduleSecureId, QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType);
}
