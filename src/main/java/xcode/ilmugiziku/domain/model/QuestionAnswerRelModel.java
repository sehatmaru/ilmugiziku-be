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
@Table(name = "t_question_answer_rel")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerRelModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "question_secure_id")
   private String question;

   @Column(name = "answer_secure_id")
   private String answer;

   @Column(name = "correct_answer")
   private boolean correctAnswer;

}
