package xcode.ilmugiziku.service;

import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
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
import xcode.ilmugiziku.presenter.PaymentPresenter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static xcode.ilmugiziku.shared.Environment.XENDIT_API;
import static xcode.ilmugiziku.shared.Environment.XENDIT_TOKEN;
import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.stringToArray;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;

@Service
public class PaymentService implements PaymentPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final PackageService packageService;

   private final PaymentRepository paymentRepository;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public PaymentService(AuthTokenService authTokenService,
                         PaymentRepository paymentRepository,
                         AuthService authService,
                         PackageService packageService) {
      this.authTokenService = authTokenService;
      this.paymentRepository = paymentRepository;
      this.authService = authService;
      this.packageService = packageService;
   }

   @Override
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
            payment.setFee(fee);
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

   @Override
   public BaseResponse<CreatePaymentResponse> createPayment(String token, CreatePaymentRequest request) {
      BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            AuthModel authModel = authService.getAuthBySecureId(authTokenService.getAuthTokenByToken(token).getAuthSecureId());
            PackageModel packageModel = packageService.getPackageByType(request.getPackageType());

            boolean isUpgrade = request.isUpgradePackage(authModel);
            int fee = isUpgrade ? packageModel.getPrice() * 50 / 100 : packageModel.getPrice();

            try {
               String secureId = generateSecureId();

               CreatePaymentResponse payment = createInvoice(packageModel, authModel, fee, secureId);

               PaymentModel model = paymentMapper.createRequestToModel(request);
               model.setSecureId(secureId);
               model.setPackageSecureId(packageModel.getSecureId());
               model.setAuthSecureId(authModel.getSecureId());
               model.setFee(fee * 6);
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

   @Override
   public BaseResponse<XenditPaymentResponse> xenditCallback(String token, XenditPaymentRequest request) {
      BaseResponse<XenditPaymentResponse> response = new BaseResponse<>();

      XenditPaymentResponse result = new XenditPaymentResponse();
      PaymentModel payment = paymentRepository.findByInvoiceIdAndDeletedAtIsNull(request.getId());

      System.out.println(token);
      System.out.println(request);

      if (payment != null && token.equals(XENDIT_TOKEN)) {
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

   private CreatePaymentResponse createInvoice(PackageModel pack, AuthModel auth, int fee, String secureId) {
      CreatePaymentResponse response = new CreatePaymentResponse();

      XenditClient xenditClient = new XenditClient.Builder()
              .setApikey(XENDIT_API)
              .build();

      try {
         Map<String, Object> params = new HashMap<>();
         params.put("external_id", secureId);
         params.put("amount", fee);
         params.put("payer_email", auth.getEmail());
         params.put("currency", "IDR");
         params.put("should_send_email", true);

         Invoice invoice = xenditClient.invoice.create(params);

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
}