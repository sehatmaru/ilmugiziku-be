package xcode.marsiajar.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_webinar")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class WebinarModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "link")
   private String link;

   @Column(name = "category_secure_id")
   private String category;

   @Column(name = "title")
   private String title;

   @Column(name = "date")
   private Date date;

   @Column(name = "meeting_id")
   private String meetingId;

   @Column(name = "available")
   private boolean available;

   @Column(name = "rating")
   private double rating;

   @Column(name = "price")
   private BigDecimal price;

   @Column(name = "passcode")
   private String passcode;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

}
