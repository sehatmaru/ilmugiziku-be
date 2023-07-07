package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.model.CourseModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.model.UserCourseRelModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.repository.CourseRepository;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.repository.UserCourseRepository;
import xcode.ilmugiziku.domain.repository.UserRepository;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.PaymentMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.*;

@Service
public class PaymentService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private CourseRepository courseRepository;
   @Autowired private UserCourseRepository userCourseRepository;
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

   // TODO: 05/07/23
   @Deprecated
   public boolean isUpgradePackage(UserModel userModel, CourseTypeEnum packageType) {
      boolean result = false;

//      if (userModel.isPremium()) {
//         if (packageType == UKOM_EXPERT) {
//            result = checkPackage(userModel, UKOM_NEWBIE);
//         }
//
//         if (packageType == SKB_EXPERT) {
//            result = checkPackage(userModel, SKB_NEWBIE);
//         }
//      }

      return result;
   }

   // TODO: 05/07/23
   private boolean checkPackage(UserModel userModel, CourseTypeEnum packageType) {
//      for (String type : stringToArray(userModel.getPackages())) {
//         if (CourseTypeEnum.valueOf(type) == packageType) {
//            return true;
//         }
//      }

      return false;
   }

}