package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.ExamModel;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String> {
    ExamModel findBySecureIdAndDeletedAtIsNull(String secureId);

    ExamModel findBySecureId(String secureId);

    List<ExamModel> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("SELECT NEW ExamModel(CASE WHEN MAX(ue.user) = :user THEN :user ELSE '' END," +
            " MAX(ue.finishTime), MAX(e.secureId), MAX(e.startAt), MAX(e.endAt), MAX(e.title), MAX(e.available), MAX(e.currentParticipant), MAX(e.category))" +
            " FROM ExamModel e" +
            " LEFT JOIN UserExamRelModel ue ON e.secureId = ue.exam" +
            " WHERE ue.user = :user or ue.user is null and e.deletedAt IS NULL" +
            " GROUP BY e.secureId, ue.user" +
            " ORDER BY MAX(ue.user) DESC, MAX(e.startAt)")
    List<ExamModel> getAllSorted(@Param("user") String user);
}
