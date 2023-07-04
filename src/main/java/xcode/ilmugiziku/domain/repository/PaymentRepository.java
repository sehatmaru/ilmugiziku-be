package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.PaymentModel;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

   PaymentModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<PaymentModel> findAllByUserSecureIdAndPaymentStatusAndDeletedAtIsNull(String secureId, String status);

   PaymentModel findByUserSecureIdAndPackageSecureIdAndDeletedAtIsNull(String user, String pack);

   PaymentModel findByInvoiceIdAndDeletedAtIsNull(String invoice);

   PaymentModel findByUserSecureIdAndPackageTypeAndDeletedAtIsNull(String secureId, int type);

   PaymentModel findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(String secureId, int type, String status);
}
