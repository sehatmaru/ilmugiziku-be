package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;
import xcode.ilmugiziku.domain.model.PaymentModel;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

   PaymentModel findBySecureIdAndDeletedAtIsNull(String secureId);

   List<PaymentModel> findAllByUserSecureIdAndPaymentStatusAndDeletedAtIsNull(String secureId, String status);

   PaymentModel findByUserSecureIdAndCourseAndDeletedAtIsNull(String user, String course);

   PaymentModel findByInvoiceIdAndDeletedAtIsNull(String invoice);

   PaymentModel findByUserSecureIdAndPackageTypeAndDeletedAtIsNull(String secureId, CourseTypeEnum type);

   PaymentModel findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(String secureId, CourseTypeEnum type, PaymentStatusEnum status);
}
