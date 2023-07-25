package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.LearningTypeEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.CourseRepository;
import xcode.ilmugiziku.domain.repository.InvoiceRepository;
import xcode.ilmugiziku.domain.repository.UserCourseRepository;
import xcode.ilmugiziku.domain.repository.UserWebinarRepository;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.request.invoice.XenditInvoiceRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.domain.response.invoice.InvoiceListResponse;
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
   @Autowired private UserWebinarRepository userWebinarRepository;
   @Autowired private Environment environment;

   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   /**
    * get invoices list
    * @return response
    */
   public BaseResponse<List<InvoiceListResponse>> list() {
      BaseResponse<List<InvoiceListResponse>> response = new BaseResponse<>();

      List<InvoiceModel> invoices = invoiceRepository.findAll();

      try {
         List<InvoiceListResponse> responses = invoiceMapper.modelsToListResponses(invoices);
         responses.forEach(e -> {
            String userSecureId = e.isCourseInvoice()
                    ? userCourseRepository.getUserCourseBySecureId(e.getRelSecureId()).getUser()
                    : userWebinarRepository.getUserWebinarBySecureId(e.getRelSecureId()).getUser();

            e.setConsumerName(profileService.getUserFullName(userSecureId));
         });

         response.setSuccess(responses);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<InvoiceResponse> detailInvoice(CourseTypeEnum packageType) {
      BaseResponse<InvoiceResponse> response = new BaseResponse<>();

      CourseModel model = courseRepository.findByCourseTypeAndDeletedAtIsNull(packageType);

      if (model == null) {
         throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      }

      try {
         InvoiceResponse invoice = new InvoiceResponse();
         invoice.setTotalAmount(model.getPrice());
         invoice.setTitle(model.getTitle());

         response.setSuccess(invoice);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * get the xendit callback,
    * it will trigger by xendit invoice callback
    * and will insert invoice to t_invoice
    * based on the callback request.
    * it can be webinar/course invoice
    * @param request body
    * @return invoice
    */
   public BaseResponse<XenditInvoiceResponse> xenditCallback(XenditInvoiceRequest request) {
      BaseResponse<XenditInvoiceResponse> response = new BaseResponse<>();

      XenditInvoiceResponse result = new XenditInvoiceResponse();
      InvoiceModel invoice = invoiceRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (invoice != null) {
         UserCourseRelModel userCourse = userCourseRepository.getActiveUserCourseBySecureId(invoice.getUserCourse());
         UserWebinarRelModel userWebinar = userWebinarRepository.getActiveUserWebinarBySecureId(invoice.getUserWebinar());

         if (invoice.isCourseInvoice()) setUserCourse(userCourse, request.isPaid());
         else setUserWebinar(userWebinar, request.isPaid());

         if (request.isPaid()) {
            invoice.setPaidDate(new Date());
            invoice.setPaymentMethod(request.getPaymentMethod());
            invoice.setPaymentChannel(request.getPaymentChannel());
            invoice.setBankCode(request.getBankCode());
         } else if (request.isExpired()) {
            invoice.setDeletedAt(new Date());
         }

         invoice.setInvoiceStatus(request.getStatus());

         try {
            invoiceRepository.save(invoice);

            if (invoice.isCourseInvoice()) userCourseRepository.save(userCourse);
            else userWebinarRepository.save(userWebinar);

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

   private void setUserWebinar(UserWebinarRelModel model, boolean isPaid) {
      if (isPaid) {
         model.setActive(true);
         model.setExpireAt(getNextMonthDate());
      } else model.setDeleted(true);
   }

   private void setUserCourse(UserCourseRelModel model, boolean isPaid) {
      if (isPaid) {
         model.setActive(true);
         model.setExpireAt(getNextMonthDate());
      } else model.setDeleted(true);
   }

   /**
    * create the purchase invoice
    * @param user body
    * @param request body
    * @param webinarModel body
    * @param courseModel body
    * @param type type
    * @param secureId = invoice secure id
    * @return response
    */
   public PurchaseResponse createInvoice(UserModel user,
                                         PurchaseRequest request,
                                         WebinarModel webinarModel,
                                         CourseModel courseModel,
                                         LearningTypeEnum type,
                                         String secureId) {
      PurchaseResponse response = new PurchaseResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(environment.getProperty("xendit.token"))
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(
                 invoiceMapper.createInvoiceRequest(user, profileService.getUserFullName(), request, type, webinarModel, courseModel, secureId)
         );

         response.setInvoiceId(invoice.getId());
         response.setInvoiceUrl(invoice.getInvoiceUrl());
         response.setInvoiceDeadline(stringToDate(invoice.getExpiryDate()));
      } catch (XenditException e) {
         throw new AppException(e.toString());
      }

      return response;
   }

}