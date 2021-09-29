package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.VideoModel;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<VideoModel, String> {

   VideoModel findBySecureIdAndDeletedAtIsNull(String secureId);

   VideoModel findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(int questionType, int questionSubType, String secureId);

   List<VideoModel> findByTemplateSecureIdAndDeletedAtIsNull(String secureId);
}
