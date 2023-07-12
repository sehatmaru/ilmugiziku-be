package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "t_template_question_rel")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class TemplateQuestionRelModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "template_secure_id")
   private String template;

   @Column(name = "question_secure_id")
   private String question;

}
