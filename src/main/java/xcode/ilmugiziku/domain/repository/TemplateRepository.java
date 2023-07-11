package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.TemplateModel;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateModel, String> {

   TemplateModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<TemplateModel> findAllByDeletedAtIsNull();

}
