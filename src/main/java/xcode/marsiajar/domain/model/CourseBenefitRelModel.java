package xcode.marsiajar.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "t_course_benefit_rel")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class CourseBenefitRelModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "course_secure_id")
   private String course;

   @Column(name = "benefit_secure_id")
   private String benefit;

   @Column(name = "deleted")
   private boolean deleted;

}
