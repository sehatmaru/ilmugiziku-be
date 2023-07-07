package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.LessonResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.service.CourseService;
import xcode.ilmugiziku.service.LessonService;
import xcode.ilmugiziku.service.RatingService;

import java.util.List;

@RestController
@RequestMapping(value = "course")
public class CourseApi {

    @Autowired private RatingService ratingService;
    @Autowired private CourseService courseService;
    @Autowired private LessonService lessonService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<CourseResponse>>> list() {
        BaseResponse<List<CourseResponse>> response = courseService.getCourseList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/lesson/rating/set")
    ResponseEntity<BaseResponse<Double>> submitRating(
            @RequestParam @Validated String lessonSecureId,
            @RequestBody @Validated SubmitRatingRequest body
    ) {
        BaseResponse<Double> response = ratingService.submitRating(lessonSecureId, body);

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

    @GetMapping("/lesson")
    ResponseEntity<BaseResponse<LessonResponse>> getLesson(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<LessonResponse> response = lessonService.getLesson(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package")
    ResponseEntity<BaseResponse<CourseResponse>> getCourse(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<CourseResponse> response = courseService.getCourse(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/reminder")
    ResponseEntity<BaseResponse<Boolean>> sendWebinarReminder(
            @RequestParam @Validated String webinarSecureId
    ) {
        BaseResponse<Boolean> response = courseService.sendWebinarReminder(webinarSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
