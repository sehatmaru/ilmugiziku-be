package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.PackageRepository;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.PaymentMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.Environment.XENDIT_API;
import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.stringToArray;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;

@Service
public class PaymentService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthRepository authRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private PackageRepository packageRepository;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public BaseResponse<PaymentResponse> detailPayment(String token, int packageType) {
      BaseResponse<PaymentResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthModel authModel = authRepository.findBySecureId(authTokenService.getAuthTokenByToken(token).getAuthSecureId());
         PackageModel packageModel = packageRepository.findByPackageTypeAndDeletedAtIsNull(packageType);

         try {
            boolean isUpgrade = isUpgradePackage(authModel, packageType);
            int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();

            PackageModel model = packageRepository.findByPackageTypeAndDeletedAtIsNull(packageType);

            PaymentResponse payment = new PaymentResponse();
            payment.setUpgrade(isUpgrade);
            payment.setFee(fee * 6);
            payment.setPackageName(model.getTitle());

            response.setSuccess(payment);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreatePaymentResponse> createPayment(String token, CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthModel authModel = authRepository.findBySecureId(authTokenService.getAuthTokenByToken(token).getAuthSecureId());
         PackageModel packageModel = packageRepository.findByPackageTypeAndDeletedAtIsNull(request.getPackageType());

         boolean isUpgrade = isUpgradePackage(authModel, request.getPackageType());
         int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();
         fee *= 6;

         try {
            String secureId = generateSecureId();

            CreatePaymentResponse payment = createInvoice(authModel, request, packageModel, fee, secureId);

            PaymentModel model = paymentMapper.createRequestToModel(request ,payment);
            model.setSecureId(secureId);
            model.setPackageSecureId(packageModel.getSecureId());
            model.setAuthSecureId(authModel.getSecureId());
            model.setFee(fee);
            model.setUpgrade(isUpgrade);

            paymentRepository.save(model);

            response.setSuccess(payment);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<XenditPaymentResponse> xenditCallback(XenditPaymentRequest request) {
      BaseResponse<XenditPaymentResponse> response = new BaseResponse<>();

      XenditPaymentResponse result = new XenditPaymentResponse();
      PaymentModel payment = paymentRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (payment != null) {
         AuthModel authModel = authRepository.findBySecureId(payment.getAuthSecureId());

         if (request.isPaid()) {
            String packages = authModel.getPackages() != null ? authModel.getPackages() : "";
            packages += String.valueOf(payment.getPackageType());

            authModel.setPackages(packages);

            payment.setPaidDate(new Date());

            savePreviousPayment(payment, authModel);
         } else if (request.isExpired()) {
            payment.setDeletedAt(new Date());
         }

         payment.setPaymentStatus(request.getStatus());

         try {
            paymentRepository.save(payment);
            authRepository.save(authModel);

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

   private void savePreviousPayment(PaymentModel payment, AuthModel authModel) {
      if (payment.isUpgrade()) {
         int prevType = payment.getPackageType() == UKOM_EXPERT ? UKOM_NEWBIE : SKB_NEWBIE;
         PaymentModel prevPayment = paymentRepository.findByAuthSecureIdAndPackageTypeAndDeletedAtIsNull(authModel.getSecureId(), prevType);
         prevPayment.setDeletedAt(new Date());

         paymentRepository.save(prevPayment);
         payment.setExpiredDate(prevPayment.getExpiredDate());
      }
   }

   private CreatePaymentResponse createInvoice(AuthModel auth,
                                               CreatePaymentRequest request,
                                               PackageModel packageModel,
                                               int fee,
                                               String secureId) {
      CreatePaymentResponse response = new CreatePaymentResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(XENDIT_API)
              .build();

      try {
         Invoice invoice = xenditClient.invoice.create(paymentMapper.createInvoiceRequest(auth, request, packageModel, fee, secureId));

         response.setInvoiceId(invoice.getId());
         response.setInvoiceUrl(invoice.getInvoiceUrl());
         response.setPaymentDeadline(invoice.getExpiryDate());
      } catch (XenditException e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public boolean isUpgradePackage(AuthModel authModel, int packageType) {
      boolean result = false;

      if (authModel.isPremium()) {
         if (packageType == UKOM_EXPERT) {
            result = checkPackage(authModel, UKOM_NEWBIE);
         }

         if (packageType == SKB_EXPERT) {
            result = checkPackage(authModel, SKB_NEWBIE);
         }
      }

      return result;
   }

   private boolean checkPackage(AuthModel authModel, int packageType) {
      for (String type : stringToArray(authModel.getPackages())) {
         if (Integer.parseInt(type) == packageType) {
            return true;
         }
      }

      return false;
   }

}