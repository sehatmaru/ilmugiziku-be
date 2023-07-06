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
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.repository.CourseRepository;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
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
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.stringToArray;

@Service
public class PaymentService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private CourseRepository courseRepository;
   @Autowired private Environment environment;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public BaseResponse<PaymentResponse> detailPayment(CourseTypeEnum packageType) {
      BaseResponse<PaymentResponse> response = new BaseResponse<>();

      CourseModel model = courseRepository.findByCourseTypeAndDeletedAtIsNull(packageType);

      if (model == null) {
         throw new AppException(PACKAGE_NOT_FOUND_MESSAGE);
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

   public BaseResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      CourseModel courseModel = courseRepository.findByCourseTypeAndDeletedAtIsNull(request.getPackageType());

      if (courseModel == null) throw new AppException(PACKAGE_NOT_FOUND_MESSAGE);

      try {
         String secureId = generateSecureId();

         CreatePaymentResponse payment = createInvoice(userModel, request, courseModel, courseModel.getPrice(), secureId);

         PaymentModel model = paymentMapper.createRequestToModel(request ,payment);
         model.setSecureId(secureId);
         model.setCourse(courseModel.getSecureId());
         model.setUserSecureId(userModel.getSecureId());
         model.setTotalAmount(courseModel.getPrice());

         paymentRepository.save(model);

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
         UserModel userModel = userRepository.findBySecureId(payment.getUserSecureId());

         // TODO: 05/07/23
         if (request.isPaid()) {
//            String packages = userModel.getPackages() != null ? userModel.getPackages() : "";
//            packages += String.valueOf(payment.getPackageType());

//            userModel.setPackages(packages);

            payment.setPaidDate(new Date());
         } else if (request.isExpired()) {
            payment.setDeletedAt(new Date());
         }

         payment.setPaymentStatus(request.getStatus());

         try {
            paymentRepository.save(payment);
            userRepository.save(userModel);

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
         response.setPaymentDeadline(invoice.getExpiryDate());
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