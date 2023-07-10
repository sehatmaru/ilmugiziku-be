package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;
import xcode.ilmugiziku.domain.model.PaymentModel;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

   PaymentModel findBySecureIdAndDeletedAtIsNull(String secureId);
   PaymentModel findByInvoiceIdAndDeletedAtIsNull(String invoice);

   @Query(value = "SELECT * FROM t_payment" +
           " WHERE payment_status IN ('PENDING') AND deleted_at IS NULL", nativeQuery = true)
   List<PaymentModel> getAllPendingPayment();

   @Query(value = "SELECT * FROM t_payment p" +
           " LEFT JOIN t_user_course_rel uc ON uc.secure_id = p.user_course_secure_id" +
           " WHERE p.payment_status IN ('PENDING')" +
           " AND uc.course_secure_id = :course" +
           " AND p.deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   PaymentModel getPendingCoursePayment(String course);

}
