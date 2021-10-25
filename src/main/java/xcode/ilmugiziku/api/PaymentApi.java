package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.presenter.PaymentPresenter;

@RestController
@RequestMapping(value = "payment")
public class PaymentApi {

    final PaymentPresenter paymentPresenter;

    public PaymentApi(PaymentPresenter paymentPresenter) {
        this.paymentPresenter = paymentPresenter;
    }

    @PostMapping("/create")
    ResponseEntity<BaseResponse<CreatePaymentResponse>> list(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreatePaymentRequest request
    ) {
        BaseResponse<CreatePaymentResponse> response = paymentPresenter.createPayment(token, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/detail")
    ResponseEntity<BaseResponse<PaymentResponse>> detail(
            @RequestParam @Validated String token,
            @RequestParam @Validated int packageType
    ) {
        BaseResponse<PaymentResponse> response = paymentPresenter.detailPayment(token, packageType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/xendit/callback")
    ResponseEntity<BaseResponse<XenditPaymentResponse>> xenditCallback(
            @RequestBody @Validated XenditPaymentRequest request
    ) {
        BaseResponse<XenditPaymentResponse> response = paymentPresenter.xenditCallback(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
