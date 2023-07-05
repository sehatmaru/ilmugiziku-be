package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.BimbelTypeEnum;
import xcode.ilmugiziku.domain.model.WebinarModel;

import java.util.List;

@Repository
public interface WebinarRepository extends JpaRepository<WebinarModel, String> {

   WebinarModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<WebinarModel> findAllByBimbelTypeAndDeletedAtIsNull(BimbelTypeEnum type);
}
