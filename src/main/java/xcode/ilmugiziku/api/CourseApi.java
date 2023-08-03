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
import xcode.ilmugiziku.domain.response.course.CourseListResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.service.CourseService;

import java.util.List;

@RestController
@RequestMapping(value = "course")
public class CourseApi {

    @Autowired private CourseService courseService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<CourseListResponse>>> list(
            @RequestParam @Validated String title,
            @RequestParam @Validated String category
    ) {
        BaseResponse<List<CourseListResponse>> response = courseService.getCourseList(title, category);

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

    @GetMapping("/rate")
    ResponseEntity<BaseResponse<Boolean>> rate(
            @RequestParam @Validated String courseSecureId,
            @RequestParam @Validated int rating
    ) {
        BaseResponse<Boolean> response = courseService.giveRating(courseSecureId, rating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
