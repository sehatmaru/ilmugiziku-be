package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.mapper.PaymentMapper;
import xcode.ilmugiziku.presenter.PaymentPresenter;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class PaymentService implements PaymentPresenter {

   private final AuthTokenService authTokenService;

   private final PaymentRepository paymentRepository;

   private final PaymentMapper paymentMapper = new PaymentMapper();

   public PaymentService(AuthTokenService authTokenService,
                         PaymentRepository paymentRepository) {
      this.authTokenService = authTokenService;
      this.paymentRepository = paymentRepository;
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
}
