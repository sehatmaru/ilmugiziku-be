package xcode.marsiajar.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import xcode.marsiajar.domain.enums.CronJobTypeEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_cron_job")
@DynamicUpdate
@AllArgsConstructor
public class CronJobModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "description")
   private String description = "";

   @Column(name = "success")
   private boolean success = false;

   @Column(name = "cron_job_type")
   @Enumerated(EnumType.STRING)
   private CronJobTypeEnum cronJobType;

   @Column(name = "executed_at")
   private Date executedAt;

   @Column(name = "total_effected_data")
   private int totalEffectedData;

   public CronJobModel(CronJobTypeEnum cronJobType) {
      this.executedAt = new Date();
      this.cronJobType = cronJobType;
   }

   public CronJobModel() {

   }
}
