package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;

public interface PaymentPresenter {
   BaseResponse<PaymentResponse> detailPayment(String token, int packageType);
   BaseResponse<CreatePaymentResponse> createPayment(String token, CreatePaymentRequest request);
   BaseResponse<XenditPaymentResponse> xenditCallback(String token, XenditPaymentRequest request);
}
