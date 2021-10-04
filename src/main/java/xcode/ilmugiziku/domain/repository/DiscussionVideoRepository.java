package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.DiscussionVideoModel;

import java.util.List;

@Repository
public interface DiscussionVideoRepository extends JpaRepository<DiscussionVideoModel, String> {

   DiscussionVideoModel findBySecureIdAndDeletedAtIsNull(String secureId);

   DiscussionVideoModel findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(int questionType, int questionSubType, String secureId);

   List<DiscussionVideoModel> findByTemplateSecureIdAndDeletedAtIsNull(String secureId);
}
