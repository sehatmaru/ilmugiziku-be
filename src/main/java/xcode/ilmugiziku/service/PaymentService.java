package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.PackageTypeEnum;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.repository.PackageRepository;
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

import static xcode.ilmugiziku.domain.enums.PackageTypeEnum.*;
import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.stringToArray;

@Service
public class PaymentService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private PackageRepository packageRepository;
   @Autowired private Environment environment;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public BaseResponse<PaymentResponse> detailPayment(PackageTypeEnum packageType) {
      BaseResponse<PaymentResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      PackageModel packageModel = packageRepository.findByPackageTypeAndDeletedAtIsNull(packageType);

      if (packageModel == null) {
         throw new AppException(PACKAGE_NOT_FOUND_MESSAGE);
      }

      try {
         boolean isUpgrade = isUpgradePackage(userModel, packageType);
         int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();

         PackageModel model = packageRepository.findByPackageTypeAndDeletedAtIsNull(packageType);

         if (model == null) {
            throw new AppException(NOT_FOUND_MESSAGE);
         } else {
            PaymentResponse payment = new PaymentResponse();
            payment.setUpgrade(isUpgrade);
            payment.setFee(fee * 6);
            payment.setPackageName(model.getTitle());

            response.setSuccess(payment);
         }
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      PackageModel packageModel = packageRepository.findByPackageTypeAndDeletedAtIsNull(request.getPackageType());

      if (packageModel == null) throw new AppException(PACKAGE_NOT_FOUND_MESSAGE);

      boolean isUpgrade = isUpgradePackage(userModel, request.getPackageType());
      int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();
      fee *= 6;

      try {
         String secureId = generateSecureId();

         CreatePaymentResponse payment = createInvoice(userModel, request, packageModel, fee, secureId);

         PaymentModel model = paymentMapper.createRequestToModel(request ,payment);
         model.setSecureId(secureId);
         model.setPackageSecureId(packageModel.getSecureId());
         model.setUserSecureId(userModel.getSecureId());
         model.setFee(fee);
         model.setUpgrade(isUpgrade);

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

         if (request.isPaid()) {
            String packages = userModel.getPackages() != null ? userModel.getPackages() : "";
            packages += String.valueOf(payment.getPackageType());

            userModel.setPackages(packages);

            payment.setPaidDate(new Date());

            savePreviousPayment(payment, userModel);
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

   private void savePreviousPayment(PaymentModel payment, UserModel userModel) {
      if (payment.isUpgrade()) {
         PackageTypeEnum prevType = payment.getPackageType() == UKOM_EXPERT ? UKOM_NEWBIE : SKB_NEWBIE;
         PaymentModel prevPayment = paymentRepository.findByUserSecureIdAndPackageTypeAndDeletedAtIsNull(userModel.getSecureId(), prevType);
         prevPayment.setDeletedAt(new Date());

         paymentRepository.save(prevPayment);
         payment.setExpiredDate(prevPayment.getExpiredDate());
      }
   }

   private CreatePaymentResponse createInvoice(UserModel user,
                                               CreatePaymentRequest request,
                                               PackageModel packageModel,
                                               int fee,
                                               String secureId) {
      CreatePaymentResponse response = new CreatePaymentResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(environment.getProperty("xendit.token"))
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(paymentMapper.createInvoiceRequest(user, profileService.getUserFullName(), request, packageModel, fee, secureId));

         response.setInvoiceId(invoice.getId());
         response.setInvoiceUrl(invoice.getInvoiceUrl());
         response.setPaymentDeadline(invoice.getExpiryDate());
      } catch (XenditException e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public boolean isUpgradePackage(UserModel userModel, PackageTypeEnum packageType) {
      boolean result = false;

      if (userModel.isPremium()) {
         if (packageType == UKOM_EXPERT) {
            result = checkPackage(userModel, UKOM_NEWBIE);
         }

         if (packageType == SKB_EXPERT) {
            result = checkPackage(userModel, SKB_NEWBIE);
         }
      }

      return result;
   }

   private boolean checkPackage(UserModel userModel, PackageTypeEnum packageType) {
      for (String type : stringToArray(userModel.getPackages())) {
         if (PackageTypeEnum.valueOf(type) == packageType) {
            return true;
         }
      }

      return false;
   }

}