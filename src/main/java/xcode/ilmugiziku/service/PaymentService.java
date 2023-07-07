package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.PaymentMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.*;

@Service
public class PaymentService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private CourseRepository courseRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private CronJobRepository cronJobRepository;
   @Autowired private Environment environment;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public BaseResponse<PaymentResponse> detailPayment(CourseTypeEnum packageType) {
      BaseResponse<PaymentResponse> response = new BaseResponse<>();

      CourseModel model = courseRepository.findByCourseTypeAndDeletedAtIsNull(packageType);

      if (model == null) {
         throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      }

      try {
         PaymentResponse payment = new PaymentResponse();
         payment.setTotalAmount(model.getPrice());
         payment.setPackageName(model.getTitle());

         response.setSuccess(payment);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<CreatePaymentResponse> createPayment(String courseSecureId, CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      if (!courseModel.isOpen()) throw new AppException(INACTIVE_COURSE);

      try {
         String paymentSecureId = generateSecureId();
         String userCourseSecureId = generateSecureId();

         CreatePaymentResponse payment = createInvoice(userModel, request, courseModel, courseModel.getPrice(), paymentSecureId);

         UserCourseRelModel userCourse = new UserCourseRelModel();
         userCourse.setSecureId(userCourseSecureId);
         userCourse.setUser(CurrentUser.get().getUserSecureId());
         userCourse.setCourse(courseSecureId);

         PaymentModel model = paymentMapper.createRequestToModel(request ,payment);
         model.setSecureId(paymentSecureId);
         model.setUserCourse(userCourseSecureId);
         model.setTotalAmount(courseModel.getPrice());

         paymentRepository.save(model);
         userCourseRepository.save(userCourse);

         response.setSuccess(payment);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<XenditPaymentResponse> xenditCallback(XenditPaymentRequest request) {
      BaseResponse<XenditPaymentResponse> response = new BaseResponse<>();

      System.out.println(request.toString());

      XenditPaymentResponse result = new XenditPaymentResponse();
      PaymentModel payment = paymentRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (payment != null) {
         UserCourseRelModel userCourse = userCourseRepository.getUserCourseBySecureId(payment.getUserCourse());

         if (request.isPaid()) {
            userCourse.setActive(true);
            userCourse.setExpireAt(getNextMonthDate());

            payment.setPaidDate(new Date());
         } else if (request.isExpired()) {
            userCourse.setDeleted(true);
            payment.setDeletedAt(new Date());
         }

         payment.setPaymentStatus(request.getStatus());

         try {
            paymentRepository.save(payment);
            userCourseRepository.save(userCourse);

            result.setInvoiceId(request.getId());
            result.setStatus(request.getStatus());
            result.setPaidDate(payment.getPaidDate());

            response.setSuccess(result);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   private CreatePaymentResponse createInvoice(UserModel user,
                                               CreatePaymentRequest request,
                                               CourseModel courseModel,
                                               int totalAmount,
                                               String secureId) {
      CreatePaymentResponse response = new CreatePaymentResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(environment.getProperty("xendit.token"))
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(paymentMapper.createInvoiceRequest(user, profileService.getUserFullName(), request, courseModel, totalAmount, secureId));

         System.out.println(invoice.toString());

         response.setInvoiceId(invoice.getId());
         response.setInvoiceUrl(invoice.getInvoiceUrl());
         response.setPaymentDeadline(stringToDate(invoice.getExpiryDate()));
      } catch (XenditException e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * will check all expired payment
    * execute at 1 am every dat
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void checkExpiredPayment() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.CHECKING_EXPIRED_PAYMENT);

      try {
         List<PaymentModel> pendingPayments = paymentRepository.getAllPendingPayment(PaymentStatusEnum.PENDING);

         int totalEffectedData = 0;

         for (PaymentModel payment: pendingPayments) {
            if (payment.getPaymentDeadline().before(new Date())) {
               payment.setDeletedAt(new Date());
               payment.setPaymentStatus(PaymentStatusEnum.EXPIRED);

               totalEffectedData += 1;
            }
         }

         paymentRepository.saveAll(pendingPayments);

         cronJobModel.setSuccess(true);
         cronJobModel.setTotalEffectedData(totalEffectedData);
      } catch (Exception e) {
         cronJobModel.setDescription(e.toString());
      }

      cronJobRepository.save(cronJobModel);
   }

}