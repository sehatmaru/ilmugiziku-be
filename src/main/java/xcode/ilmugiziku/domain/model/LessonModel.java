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
@Table(name = "lesson")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class LessonModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "video_uri")
   private String videoUri;

   @Column(name = "bimbel_type")
   private int bimbelType;

   @Column(name = "title")
   private String title;

   @Column(name = "theory")
   private String theory;

   @Column(name = "rating")
   private double rating;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;
}
