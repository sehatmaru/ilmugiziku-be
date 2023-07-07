package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

   PaymentModel findBySecureIdAndDeletedAtIsNull(String secureId);
   PaymentModel findByInvoiceIdAndDeletedAtIsNull(String invoice);

}
