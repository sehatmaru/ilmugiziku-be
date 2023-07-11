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
@Table(name = "t_question")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "content")
   private String content;

   @Column(name = "created_by")
   private String createdBy;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "edited_by")
   private String editedBy;

   @Column(name = "updated_at")
   private Date updatedAt;

}
