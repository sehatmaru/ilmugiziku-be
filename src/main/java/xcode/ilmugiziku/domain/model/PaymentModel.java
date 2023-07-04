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
@Table(name = "t_payment")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "user_secure_id")
   private String userSecureId;

   @Column(name = "package_secure_id")
   private String packageSecureId;

   @Column(name = "invoice_id")
   private String invoiceId;

   @Column(name = "invoice_url")
   private String invoiceUrl;

   @Column(name = "paid_date")
   private Date paidDate;

   @Column(name = "package_type")
   private int packageType;

   @Column(name = "payment_status")
   private String paymentStatus;

   @Column(name = "payment_deadline")
   private String paymentDeadline;

   @Column(name = "expired_date")
   private Date expiredDate;

   @Column(name = "fee")
   private int fee;

   @Column(name = "is_upgrade")
   private boolean isUpgrade;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;
}