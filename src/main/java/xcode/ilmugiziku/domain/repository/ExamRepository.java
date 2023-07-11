package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ExamModel;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String> {
    ExamModel findBySecureIdAndDeletedAtIsNull(String secureId);

    List<ExamModel> findAllByDeletedAtIsNull();
}
