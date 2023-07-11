package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_course")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "title")
   private String title;

   @Column(name = "price")
   private BigDecimal price;

   @Column(name = "available")
   private boolean available;

   @Column(name = "rating")
   private double rating;

   @Column(name = "course_type")
   @Enumerated(EnumType.STRING)
   private CourseTypeEnum courseType;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

}
