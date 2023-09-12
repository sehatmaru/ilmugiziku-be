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
@Table(name = "t_exam")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ExamModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "title")
   private String title;

   @Column(name = "category_secure_id")
   private String category;

   @Column(name = "template_secure_id")
   private String template;

   @Column(name = "available")
   private boolean available;

   @Column(name = "max_participant")
   private int maxParticipant;

   @Column(name = "current_participant")
   private int currentParticipant;

   @Column(name = "start_at")
   private Date startAt;

   @Column(name = "end_at")
   private Date endAt;

   @Column(name = "duration")
   private int duration;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

   public boolean isFull() {
      return currentParticipant >= maxParticipant;
   }
}
