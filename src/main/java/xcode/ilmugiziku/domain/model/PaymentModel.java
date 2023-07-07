package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "t_payment")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "user_course_secure_id")
   private String userCourse;

   @Column(name = "invoice_id")
   private String invoiceId;

   @Column(name = "invoice_url")
   private String invoiceUrl;

   @Column(name = "paid_date")
   private Date paidDate;

   @Column(name = "payment_status")
   private PaymentStatusEnum paymentStatus;

   @Column(name = "payment_deadline")
   private Date paymentDeadline;

   @Column(name = "total_amount")
   private int totalAmount;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;
}
