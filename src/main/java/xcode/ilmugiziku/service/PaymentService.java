package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.mapper.PaymentMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.Environment.XENDIT_API;
import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.stringToArray;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.PaymentStatusRefs.PAID;

@Service
public class PaymentService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthService authService;
   @Autowired private PackageService packageService;
   @Autowired private PaymentRepository paymentRepository;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public BaseResponse<PaymentResponse> detailPayment(String token, int packageType) {
      BaseResponse<PaymentResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthModel authModel = authService.getAuthBySecureId(authTokenService.getAuthTokenByToken(token).getAuthSecureId());
         PackageModel packageModel = packageService.getPackageByType(packageType);

         try {
            boolean isUpgrade = isUpgradePackage(authModel, packageType);
            int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();

            PackageModel model = packageService.getPackageByType(packageType);

            PaymentResponse payment = new PaymentResponse();
            payment.setUpgrade(isUpgrade);
            payment.setFee(fee * 6);
            payment.setPackageName(model.getTitle());

            response.setSuccess(payment);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreatePaymentResponse> createPayment(String token, CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            AuthModel authModel = authService.getAuthBySecureId(authTokenService.getAuthTokenByToken(token).getAuthSecureId());
            PackageModel packageModel = packageService.getPackageByType(request.getPackageType());

            boolean isUpgrade = request.isUpgradePackage(authModel);
            int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();
            fee *= 6;

            try {
               String secureId = generateSecureId();

               CreatePaymentResponse payment = createInvoice(authModel, request, packageModel, fee, secureId);

               PaymentModel model = paymentMapper.createRequestToModel(request);
               model.setSecureId(secureId);
               model.setPackageSecureId(packageModel.getSecureId());
               model.setAuthSecureId(authModel.getSecureId());
               model.setFee(fee);
               model.setUpgrade(isUpgrade);
               model.setInvoiceId(payment.getInvoiceId());
               model.setInvoiceUrl(payment.getInvoiceUrl());
               model.setPaymentDeadline(payment.getPaymentDeadline());

               paymentRepository.save(model);

               response.setSuccess(payment);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<XenditPaymentResponse> xenditCallback(XenditPaymentRequest request) {
      BaseResponse<XenditPaymentResponse> response = new BaseResponse<>();

      XenditPaymentResponse result = new XenditPaymentResponse();
      PaymentModel payment = paymentRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      if (payment != null) {
         AuthModel authModel = authService.getAuthBySecureId(payment.getAuthSecureId());

         if (request.isPaid()) {
            String packages = authModel.getPackages() != null ? authModel.getPackages() : "";
            packages += String.valueOf(payment.getPackageType());

            authModel.setPackages(packages);

            payment.setPaidDate(new Date());

            if (payment.isUpgrade()) {
               int prevType = payment.getPackageType() == UKOM_EXPERT ? UKOM_NEWBIE : SKB_NEWBIE;
               PaymentModel prevPayment = paymentRepository.findByAuthSecureIdAndPackageTypeAndDeletedAtIsNull(authModel.getSecureId(), prevType);
               prevPayment.setDeletedAt(new Date());

               paymentRepository.save(prevPayment);
               payment.setExpiredDate(prevPayment.getExpiredDate());
            }
         } else if (request.isExpired()) {
            payment.setDeletedAt(new Date());
         }

         payment.setPaymentStatus(request.getStatus());

         try {
            paymentRepository.save(payment);
            authService.saveAuthModel(authModel);

            result.setInvoiceId(request.getId());
            result.setStatus(request.getStatus());
            result.setPaidDate(payment.getPaidDate());

            response.setSuccess(result);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private CreatePaymentResponse createInvoice(AuthModel auth, CreatePaymentRequest request, PackageModel packageModel, int fee, String secureId) {
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
         e.printStackTrace();
      }

      return response;
   }

   public boolean isUpgradePackage(AuthModel authModel, int packageType) {
      boolean result = false;

      if (authModel.isPremium()) {
         if (packageType == UKOM_EXPERT) {
            for (String type : stringToArray(authModel.getPackages())) {
               if (Integer.parseInt(type) == UKOM_NEWBIE) {
                  result = true;
               }
            }
         }

         if (packageType == SKB_EXPERT) {
            for (String type : stringToArray(authModel.getPackages())) {
               if (Integer.parseInt(type) == SKB_NEWBIE) {
                  result = true;
               }
            }
         }
      }

      return result;
   }

   public PaymentModel getPaidPaymentByAuthSecureIdAndType(String secureId, int type) {
      return paymentRepository.findByAuthSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(secureId, type, PAID);
   }

   public void savePaymentModel(PaymentModel paymentModel) {
      paymentRepository.save(paymentModel);
   }
}