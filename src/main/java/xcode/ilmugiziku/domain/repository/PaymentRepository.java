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
           " WHERE payment_status = :status AND deleted_at IS NULL", nativeQuery = true)
   List<PaymentModel> getAllPendingPayment(PaymentStatusEnum status);

   @Query(value = "SELECT * FROM t_payment" +
           " WHERE payment_status = :status AND deleted_at IS NULL", nativeQuery = true)
   PaymentModel getPendingPayment(PaymentStatusEnum status);

}
