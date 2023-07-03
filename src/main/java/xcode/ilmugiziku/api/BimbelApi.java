package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelInformationResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelResponse;
import xcode.ilmugiziku.domain.response.LessonResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;
import xcode.ilmugiziku.service.*;

import java.util.List;

@RestController
@RequestMapping(value = "bimbel")
public class BimbelApi {

    @Autowired private PackageService packageService;
    @Autowired private RatingService ratingService;
    @Autowired private BimbelService bimbelService;
    @Autowired private LessonService lessonService;

    @GetMapping("/package/list")
    ResponseEntity<BaseResponse<List<PackageResponse>>> list(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<PackageResponse>> response = packageService.getPackageList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/lesson/rating/set")
    ResponseEntity<BaseResponse<Double>> submitRating(
            @RequestParam @Validated String token,
            @RequestParam @Validated String lessonSecureId,
            @RequestBody @Validated SubmitRatingRequest body
    ) {
        BaseResponse<Double> response = ratingService.submitRating(token, lessonSecureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("")
    ResponseEntity<BaseResponse<BimbelResponse>> getBimbel(
            @RequestParam @Validated String token,
            @RequestParam @Validated int bimbelType
    ) {
        BaseResponse<BimbelResponse> response = bimbelService.getBimbel(token, bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/lesson")
    ResponseEntity<BaseResponse<LessonResponse>> getLesson(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<LessonResponse> response = lessonService.getLesson(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package")
    ResponseEntity<BaseResponse<PackageResponse>> getPackage(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<PackageResponse> response = packageService.getPackage(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/information")
    ResponseEntity<BaseResponse<BimbelInformationResponse>> getBimbelInformation(
            @RequestParam @Validated String token
    ) {
        BaseResponse<BimbelInformationResponse> response = bimbelService.getBimbelInformation(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/reminder")
    ResponseEntity<BaseResponse<Boolean>> sendWebinarReminder(
            @RequestParam @Validated String token,
            @RequestParam @Validated String webinarSecureId
    ) {
        BaseResponse<Boolean> response = bimbelService.sendWebinarReminder(token, webinarSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
