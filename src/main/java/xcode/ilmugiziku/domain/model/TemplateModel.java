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
@Table(name = "t_template")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class TemplateModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "name")
   private String name;

   @Column(name = "is_used")
   private boolean isUsed;

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
