package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;
import xcode.ilmugiziku.domain.enums.InvoiceTypeEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static xcode.ilmugiziku.domain.enums.InvoiceTypeEnum.COURSE;
import static xcode.ilmugiziku.domain.enums.InvoiceTypeEnum.WEBINAR;

@Data
@Builder
@Entity
@Table(name = "t_invoice")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "user_course_secure_id")
   private String userCourse;

   @Column(name = "user_webinar_secure_id")
   private String userWebinar;

   @Column(name = "invoice_id")
   private String invoiceId;

   @Column(name = "invoice_url")
   private String invoiceUrl;

   @Column(name = "paid_date")
   private Date paidDate;

   @Column(name = "invoice_status")
   @Enumerated(EnumType.STRING)
   private InvoiceStatusEnum invoiceStatus;

   @Column(name = "invoice_type")
   @Enumerated(EnumType.STRING)
   private InvoiceTypeEnum invoiceType;

   @Column(name = "invoice_deadline")
   private Date invoiceDeadline;

   @Column(name = "total_amount")
   private BigDecimal totalAmount;

   @Column(name = "payment_method")
   private String paymentMethod;

   @Column(name = "payment_channel")
   private String paymentChannel;

   @Column(name = "bank_code")
   private String bankCode;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

   public boolean isCourseInvoice() {
      return invoiceType == COURSE;
   }

   public boolean isWebinarInvoice() {
      return invoiceType == WEBINAR;
   }
}
