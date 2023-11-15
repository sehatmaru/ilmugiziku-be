package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.CronJobModel;

@Repository
public interface CronJobRepository extends JpaRepository<CronJobModel, String> {

}
