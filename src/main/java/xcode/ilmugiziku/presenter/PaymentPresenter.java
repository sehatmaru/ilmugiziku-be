package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;

public interface PaymentPresenter {
   BaseResponse<CreateBaseResponse> createPayment(String token, CreatePaymentRequest request);
}
