package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.InvoiceModel;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceModel, String> {

   InvoiceModel findBySecureIdAndDeletedAtIsNull(String secureId);
   InvoiceModel findByInvoiceIdAndDeletedAtIsNull(String invoice);

   @Query(value = "SELECT * FROM t_invoice" +
           " WHERE invoice_status IN ('PENDING') AND deleted_at IS NULL", nativeQuery = true)
   List<InvoiceModel> getAllPendingInvoice();

   @Query(value = "SELECT * FROM t_invoice p" +
           " LEFT JOIN t_user_course_rel uc ON uc.secure_id = p.user_course_secure_id" +
           " WHERE p.invoice_status IN ('PENDING')" +
           " AND uc.course_secure_id = :course" +
           " AND p.deleted_at IS NULL" +
           " LIMIT 1", nativeQuery = true)
   InvoiceModel getPendingCourseInvoice(String course);

}
