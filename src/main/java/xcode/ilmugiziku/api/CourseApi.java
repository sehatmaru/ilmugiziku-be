package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.service.CourseService;
import xcode.ilmugiziku.service.RatingService;

import java.util.List;

@RestController
@RequestMapping(value = "course")
public class CourseApi {

    @Autowired private RatingService ratingService;
    @Autowired private CourseService courseService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<CourseResponse>>> list() {
        BaseResponse<List<CourseResponse>> response = courseService.getCourseList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/cancel")
    ResponseEntity<BaseResponse<Boolean>> cancelCourse(
            @RequestParam @Validated String courseSecureId
    ) {
        BaseResponse<Boolean> response = courseService.cancelCourse(courseSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // TODO: 11/07/23  
//    @PostMapping("/lesson/rating/set")
//    ResponseEntity<BaseResponse<Double>> submitRating(
//            @RequestParam @Validated String lessonSecureId,
//            @RequestBody @Validated SubmitRatingRequest body
//    ) {
//        BaseResponse<Double> response = ratingService.submitRating(lessonSecureId, body);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

    @GetMapping("")
    ResponseEntity<BaseResponse<List<CourseResponse>>> getUserCourses() {
        BaseResponse<List<CourseResponse>> response = courseService.getUserCourses();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/detail")
    ResponseEntity<BaseResponse<CourseResponse>> getCourseDetail(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<CourseResponse> response = courseService.getCourse(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/purchase")
    ResponseEntity<BaseResponse<PurchaseResponse>> purchase(
            @RequestParam @Validated String courseSecureId,
            @RequestBody @Validated PurchaseRequest request
    ) {
        BaseResponse<PurchaseResponse> response = courseService.purchase(courseSecureId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
