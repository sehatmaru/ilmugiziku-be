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
@Table(name = "t_answer")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class AnswerModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "content")
   private String content;

   @Column(name = "question_secure_id")
   private String question;

   @Column(name = "correct_answer")
   private boolean correctAnswer;

}
