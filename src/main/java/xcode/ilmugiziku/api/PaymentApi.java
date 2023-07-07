package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.request.payment.XenditPaymentRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;
import xcode.ilmugiziku.domain.response.payment.PaymentResponse;
import xcode.ilmugiziku.domain.response.payment.XenditPaymentResponse;
import xcode.ilmugiziku.service.PaymentService;

@RestController
@RequestMapping(value = "payment")
public class PaymentApi {

    @Autowired private PaymentService paymentService;

    @PostMapping("/create")
    ResponseEntity<BaseResponse<CreatePaymentResponse>> create(
            @RequestParam @Validated String courseSecureId,
            @RequestBody @Validated CreatePaymentRequest request
    ) {
        BaseResponse<CreatePaymentResponse> response = paymentService.createPayment(courseSecureId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/detail")
    ResponseEntity<BaseResponse<PaymentResponse>> detail(
            @RequestParam @Validated CourseTypeEnum packageType
    ) {
        BaseResponse<PaymentResponse> response = paymentService.detailPayment(packageType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/xendit/callback")
    ResponseEntity<BaseResponse<XenditPaymentResponse>> xenditCallback(
            @RequestBody @Validated XenditPaymentRequest request
    ) {
        BaseResponse<XenditPaymentResponse> response = paymentService.xenditCallback(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
