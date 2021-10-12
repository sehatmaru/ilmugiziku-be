package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.XenditPaymentResponse;

public interface PaymentPresenter {
   BaseResponse<CreateBaseResponse> createPayment(String token, CreatePaymentRequest request);
   BaseResponse<XenditPaymentResponse> xenditCallback(String token, XenditPaymentRequest request);
}
