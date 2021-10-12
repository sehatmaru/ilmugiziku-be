package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.XenditPaymentResponse;
import xcode.ilmugiziku.mapper.PaymentMapper;
import xcode.ilmugiziku.presenter.PaymentPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.Environment.XENDIT_TOKEN;
import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class PaymentService implements PaymentPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;

   private final PaymentRepository paymentRepository;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public PaymentService(AuthTokenService authTokenService,
                         PaymentRepository paymentRepository,
                         AuthService authService) {
      this.authTokenService = authTokenService;
      this.paymentRepository = paymentRepository;
      this.authService = authService;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createPayment(String token, CreatePaymentRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);

         try {
            PaymentModel model = paymentMapper.createRequestToModel(request);
            model.setAuthSecureId(authTokenModel.getAuthSecureId());
            paymentRepository.save(model);

            createResponse.setSecureId(model.getSecureId());

            response.setSuccess(createResponse);
         } catch (Exception e){
            response.setFailed(e.toString());
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
            String packages = authModel.getPackages();
            packages += payment.getPackageType();

            authModel.setPackages(packages);

            payment.setPaidDate(new Date());
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
}
