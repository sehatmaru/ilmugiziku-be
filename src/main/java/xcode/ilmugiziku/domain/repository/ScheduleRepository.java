package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.ScheduleModel;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleModel, String> {

   ScheduleModel findBySecureId(String secureId);

   List<ScheduleModel> findByAuthSecureIdAndDeletedAtIsNullOrderBySchedule(String authSecureId);

   ScheduleModel findByScheduleAndAuthSecureIdAndDeletedAtIsNull(Date schedule, String authSecureId);
}
