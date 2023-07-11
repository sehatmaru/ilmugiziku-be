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
@Table(name = "t_rating")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class RatingModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "webinar_secure_id")
   private String webinar;

   @Column(name = "course_secure_id")
   private String course;

   @Column(name = "user_secure_id")
   private String user;

   @Column(name = "rating")
   private int rating;

   @Column(name = "rated_at")
   private Date ratedAt;

}
