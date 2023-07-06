package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.CronJobModel;

@Repository
public interface CronJobRepository extends JpaRepository<CronJobModel, String> {

}
