package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.presenter.PaymentPresenter;

@RestController
@RequestMapping(value = "payment")
public class PaymentApi {

    final PaymentPresenter paymentPresenter;

    public PaymentApi(PaymentPresenter paymentPresenter) {
        this.paymentPresenter = paymentPresenter;
    }

    @PostMapping("/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> list(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreatePaymentRequest request
    ) {
        BaseResponse<CreateBaseResponse> response = paymentPresenter.createPayment(token, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
