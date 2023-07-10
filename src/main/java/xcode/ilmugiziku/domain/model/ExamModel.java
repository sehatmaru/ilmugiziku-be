package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_exam")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ExamModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "questions_secure_id")
   private String questions;

   @Column(name = "answers_secure_id")
   private String answers;

   @Column(name = "score")
   private int score;

   @Column(name = "blank")
   private int blank;

   @Column(name = "correct")
   private int correct;

   @Column(name = "incorrect")
   private int incorrect;

   @Column(name = "user_secure_id")
   private String userSecureId;

   @Column(name = "schedule_secure_id")
   private String scheduleSecureId;

   @Column(name = "question_type")
   @Enumerated(EnumType.STRING)
   private QuestionTypeEnum questionType;

   @Column(name = "question_sub_type")
   @Enumerated(EnumType.STRING)
   private QuestionSubTypeEnum questionSubType;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

}
