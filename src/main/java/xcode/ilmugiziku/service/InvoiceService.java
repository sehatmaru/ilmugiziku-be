package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.CourseRepository;
import xcode.ilmugiziku.domain.repository.CronJobRepository;
import xcode.ilmugiziku.domain.repository.InvoiceRepository;
import xcode.ilmugiziku.domain.repository.UserCourseRepository;
import xcode.ilmugiziku.domain.request.course.PurchaseCourseRequest;
import xcode.ilmugiziku.domain.request.invoice.XenditInvoiceRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.course.PurchaseCourseResponse;
import xcode.ilmugiziku.domain.response.invoice.InvoiceResponse;
import xcode.ilmugiziku.domain.response.invoice.XenditInvoiceResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.InvoiceMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.COURSE_NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.INVOICE_NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.getNextMonthDate;
import static xcode.ilmugiziku.shared.Utils.stringToDate;

@Service
public class InvoiceService {

   @Autowired private ProfileService profileService;
   @Autowired private InvoiceRepository invoiceRepository;
   @Autowired private CourseRepository courseRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private CronJobRepository cronJobRepository;
   @Autowired private Environment environment;

   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   public BaseResponse<InvoiceResponse> detailInvoice(CourseTypeEnum packageType) {
      BaseResponse<InvoiceResponse> response = new BaseResponse<>();

      CourseModel model = courseRepository.findByCourseTypeAndDeletedAtIsNull(packageType);

      if (model == null) {
         throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      }

      try {
         InvoiceResponse invoice = new InvoiceResponse();
         invoice.setTotalAmount(model.getPrice());
         invoice.setPackageName(model.getTitle());

         response.setSuccess(invoice);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<XenditInvoiceResponse> xenditCallback(XenditInvoiceRequest request) {
      BaseResponse<XenditInvoiceResponse> response = new BaseResponse<>();

      XenditInvoiceResponse result = new XenditInvoiceResponse();
      InvoiceModel invoice = invoiceRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (invoice != null) {
         UserCourseRelModel userCourse = userCourseRepository.getUserCourseBySecureId(invoice.getUserCourse());

         if (request.isPaid()) {
            userCourse.setActive(true);
            userCourse.setExpireAt(getNextMonthDate());

            invoice.setPaidDate(new Date());
            invoice.setPaymentMethod(request.getPaymentMethod());
            invoice.setPaymentChannel(request.getPaymentChannel());
            invoice.setBankCode(request.getBankCode());
         } else if (request.isExpired()) {
            userCourse.setDeleted(true);
            invoice.setDeletedAt(new Date());
         }

         invoice.setInvoiceStatus(request.getStatus());

         try {
            invoiceRepository.save(invoice);
            userCourseRepository.save(userCourse);

            result.setInvoiceId(request.getId());
            result.setStatus(request.getStatus());
            result.setPaidDate(invoice.getPaidDate());

            response.setSuccess(result);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(INVOICE_NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public PurchaseCourseResponse createInvoice(UserModel user,
                                                PurchaseCourseRequest request,
                                                CourseModel courseModel,
                                                String secureId) {
      PurchaseCourseResponse response = new PurchaseCourseResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(environment.getProperty("xendit.token"))
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(invoiceMapper.createInvoiceRequest(user, profileService.getUserFullName(), request, courseModel, secureId));

         System.out.println(invoice.toString());

         response.setInvoiceId(invoice.getId());
         response.setInvoiceUrl(invoice.getInvoiceUrl());
         response.setInvoiceDeadline(stringToDate(invoice.getExpiryDate()));
      } catch (XenditException e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * will check all expired invoice
    * execute at 1 am every dat
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void checkExpiredInvoice() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.CHECKING_EXPIRED_PAYMENT);

      try {
         List<InvoiceModel> pendingInvoices = invoiceRepository.getAllPendingInvoice();

         int totalEffectedData = 0;

         for (InvoiceModel invoice: pendingInvoices) {
            if (invoice.getInvoiceDeadline().before(new Date())) {
               invoice.setDeletedAt(new Date());
               invoice.setInvoiceStatus(InvoiceStatusEnum.EXPIRED);

               totalEffectedData += 1;
            }
         }

         invoiceRepository.saveAll(pendingInvoices);

         cronJobModel.setSuccess(true);
         cronJobModel.setTotalEffectedData(totalEffectedData);
      } catch (Exception e) {
         cronJobModel.setDescription(e.toString());
      }

      cronJobRepository.save(cronJobModel);
   }

}