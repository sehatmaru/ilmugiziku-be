package xcode.marsiajar.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_user_exam_rel")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserExamRelModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "user_secure_id")
   private String user;

   @Column(name = "exam_secure_id")
   private String exam;

   @Column(name = "score")
   private double score = 0.0;

   @Column(name = "correct_answer")
   private int correct = 0;

   @Column(name = "incorrect_answer")
   private int incorrect = 0;

   @Column(name = "blank_answer")
   private int blank = 0;

   @Column(name = "start_time")
   private Date startTime;

   @Column(name = "finish_time")
   private Date finishTime;

   @Column(name = "duration")
   private int duration = 0;

   @Column(name = "ranking")
   private int ranking = 0;

   @Column(name = "deleted")
   private boolean deleted = false;

}
