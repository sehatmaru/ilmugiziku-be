package xcode.ilmugiziku.domain.model;

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
@Table(name = "exam")
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

   @Column(name = "auth_secure_id")
   private String authSecureId;

   @Column(name = "question_type")
   private int questionType;

   @Column(name = "question_sub_type")
   private int questionSubType;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

}
