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
@Table(name = "t_user_course_rel")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseRelModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "user_secure_id")
   private String user;

   @Column(name = "course_secure_id")
   private String course;

   @Column(name = "active")
   private boolean active;

   @Column(name = "expire_at")
   private Date expireAt;

   @Column(name = "deleted")
   private boolean deleted;

}
