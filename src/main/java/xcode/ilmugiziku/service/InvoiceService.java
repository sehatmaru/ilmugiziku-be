package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.invoice.CreateInvoiceRequest;
import xcode.ilmugiziku.domain.request.invoice.XenditInvoiceRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.invoice.CreateInvoiceResponse;
import xcode.ilmugiziku.domain.response.invoice.InvoiceResponse;
import xcode.ilmugiziku.domain.response.invoice.XenditInvoiceResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.InvoiceMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.*;

@Service
public class InvoiceService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;
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

   public BaseResponse<CreateInvoiceResponse> createInvoice(String courseSecureId, CreateInvoiceRequest request) {
      BaseResponse<CreateInvoiceResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);
      UserCourseRelModel userCourse = userCourseRepository.getActiveUserCourse(CurrentUser.get().getUserSecureId(), courseSecureId);
      InvoiceModel unpaidInvoice = invoiceRepository.getPendingCourseInvoice(courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      if (!courseModel.isOpen()) throw new AppException(INACTIVE_COURSE);
      if (userCourse != null) throw new AppException(USER_COURSE_EXIST);

      if (unpaidInvoice != null) {
         CreateInvoiceResponse resp = new CreateInvoiceResponse();
         resp.setInvoiceDeadline(unpaidInvoice.getInvoiceDeadline());
         resp.setInvoiceId(unpaidInvoice.getInvoiceId());
         resp.setInvoiceUrl(unpaidInvoice.getInvoiceUrl());

         response.setSuccess(resp);
         response.setMessage(INVOICE_EXIST);
         response.setStatusCode(HttpStatus.CONFLICT.value());
      } else {
         try {
            checkCurrentPendingInvoice(courseSecureId);

            String invoiceSecureId = generateSecureId();
            String userCourseSecureId = generateSecureId();

            CreateInvoiceResponse invoice = createInvoice(userModel, request, courseModel, courseModel.getPrice(), invoiceSecureId);

            UserCourseRelModel userCourseModel = new UserCourseRelModel();
            userCourseModel.setSecureId(userCourseSecureId);
            userCourseModel.setUser(CurrentUser.get().getUserSecureId());
            userCourseModel.setCourse(courseSecureId);

            InvoiceModel model = invoiceMapper.createRequestToModel(request ,invoice);
            model.setSecureId(invoiceSecureId);
            model.setUserCourse(userCourseSecureId);
            model.setTotalAmount(courseModel.getPrice());

            invoiceRepository.save(model);
            userCourseRepository.save(userCourseModel);

            response.setSuccess(invoice);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      }

      return response;
   }

   private void checkCurrentPendingInvoice(String course) {
      InvoiceModel pendingInvoice = invoiceRepository.getPendingCourseInvoice(course);

      if (pendingInvoice != null) {
         pendingInvoice.setInvoiceStatus(InvoiceStatusEnum.EXPIRED);
         pendingInvoice.setDeletedAt(new Date());

         UserCourseRelModel userCourse = userCourseRepository.getUserCourseBySecureId(pendingInvoice.getUserCourse());
         userCourse.setDeleted(true);

         invoiceRepository.save(pendingInvoice);
         userCourseRepository.save(userCourse);
      }
   }

   public BaseResponse<XenditInvoiceResponse> xenditCallback(XenditInvoiceRequest request) {
      BaseResponse<XenditInvoiceResponse> response = new BaseResponse<>();

      System.out.println(request.toString());

      XenditInvoiceResponse result = new XenditInvoiceResponse();
      InvoiceModel invoice = invoiceRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (invoice != null) {
         UserCourseRelModel userCourse = userCourseRepository.getUserCourseBySecureId(invoice.getUserCourse());

         if (request.isPaid()) {
            userCourse.setActive(true);
            userCourse.setExpireAt(getNextMonthDate());

            invoice.setPaidDate(new Date());
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

   private CreateInvoiceResponse createInvoice(UserModel user,
                                               CreateInvoiceRequest request,
                                               CourseModel courseModel,
                                               int totalAmount,
                                               String secureId) {
      CreateInvoiceResponse response = new CreateInvoiceResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(environment.getProperty("xendit.token"))
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(invoiceMapper.createInvoiceRequest(user, profileService.getUserFullName(), request, courseModel, totalAmount, secureId));

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