package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.service.CronService;
import xcode.ilmugiziku.service.WebinarService;

@RestController
@RequestMapping(value = "webinar")
public class WebinarApi {

    @Autowired private WebinarService webinarService;
    @Autowired private CronService cronService;

    @PostMapping("/purchase")
    ResponseEntity<BaseResponse<PurchaseResponse>> purchase(
            @RequestParam @Validated String webinarSecureId,
            @RequestBody @Validated PurchaseRequest request
    ) {
        BaseResponse<PurchaseResponse> response = webinarService.purchase(webinarSecureId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/rate")
    ResponseEntity<BaseResponse<Boolean>> rate(
            @RequestParam @Validated String webinarSecureId,
            @RequestParam @Validated int rating
    ) {
        BaseResponse<Boolean> response = webinarService.giveRating(webinarSecureId, rating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/schedule")
    ResponseEntity<BaseResponse<Boolean>> schedule(
    ) {
        cronService.sendWebinarReminders();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse<>());
    }
}
