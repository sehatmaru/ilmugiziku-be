package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.model.TheoryModel;

import java.util.List;

@Repository
public interface TheoryRepository extends JpaRepository<TheoryModel, String> {

   TheoryModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<TheoryModel> findByTheoryTypeAndDeletedAtIsNull(TheoryTypeEnum theoryType);
}
