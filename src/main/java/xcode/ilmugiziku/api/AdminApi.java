package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.benefit.CreateUpdateBenefitRequest;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.request.template.CreateTemplateRequest;
import xcode.ilmugiziku.domain.request.template.UpdateTemplateRequest;
import xcode.ilmugiziku.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;
import xcode.ilmugiziku.domain.response.benefit.BenefitResponse;
import xcode.ilmugiziku.domain.response.user.UserResponse;
import xcode.ilmugiziku.service.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    @Autowired private QuestionService questionService;
    @Autowired private UserService userService;
    @Autowired private TemplateService templateService;
    @Autowired private BenefitService benefitService;
    @Autowired private CourseService courseService;
    @Autowired private WebinarService webinarService;

    // TODO: 11/07/23
//    @PostMapping("/question/create")
//    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (
//            @RequestBody @Validated CreateQuestionRequest body
//    ) {
//        BaseResponse<CreateBaseResponse> response = questionService.createQuestion(body);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

    // TODO: 11/07/23
//    @PutMapping("/question/update")
//    ResponseEntity<BaseResponse<Boolean>> updateQuestion(
//            @RequestBody @Validated UpdateQuestionRequest body
//    ) {
//        BaseResponse<Boolean> response = questionService.updateQuestion(body);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

    @DeleteMapping("/question/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteQuestion(
            @RequestParam @Validated String questionSecureId
    ) {
        BaseResponse<Boolean> response = questionService.deleteQuestion(questionSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/template-to/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTemplateTo(
            @RequestBody @Validated CreateTemplateRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = templateService.createTemplate(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/template-to/update")
    ResponseEntity<BaseResponse<Boolean>> updateTemplateTo(
            @RequestBody @Validated UpdateTemplateRequest body
    ) {
        BaseResponse<Boolean> response = templateService.updateTemplate(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/template-to/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTemplateTo(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = templateService.deleteTemplate(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // TODO: 11/07/23
//    @GetMapping("/template-to/list")
//    ResponseEntity<BaseResponse<List<TemplateResponse>>> getTemplateToList(
//            @RequestParam @Validated QuestionTypeEnum questionType,
//            @RequestParam @Validated QuestionSubTypeEnum questionSubType
//    ) {
//        BaseResponse<List<TemplateResponse>> response = templateService.getTemplateList(questionType, questionSubType);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

//    @PutMapping("/template-to/set")
//    ResponseEntity<BaseResponse<Boolean>> setTemplateToActive(
//            @RequestParam @Validated String secureId
//    ) {
//        BaseResponse<Boolean> response = templateService.setTemplateActive(secureId);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

    @PostMapping("/benefit/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createBenefit(
            @RequestBody @Validated CreateUpdateBenefitRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = benefitService.createBenefit(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/benefit/update")
    ResponseEntity<BaseResponse<Boolean>> updateBenefit(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateBenefitRequest body
    ) {
        BaseResponse<Boolean> response = benefitService.updateBenefit(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/benefit/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteBenefit(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = benefitService.deleteBenefit(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/benefit/list")
    ResponseEntity<BaseResponse<List<BenefitResponse>>> getBenefitList(
    ) {
        BaseResponse<List<BenefitResponse>> response = benefitService.getBenefitList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/course/update")
    ResponseEntity<BaseResponse<Boolean>> updateCourse(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateCourseRequest body
    ) {
        BaseResponse<Boolean> response = courseService.updateCourse(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createWebinar(
            @RequestBody @Validated CreateUpdateWebinarRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = webinarService.createWebinar(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/webinar/update")
    ResponseEntity<BaseResponse<Boolean>> updateWebinar(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateWebinarRequest body
    ) {
        BaseResponse<Boolean> response = webinarService.updateWebinar(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/webinar/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteWebinar(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = webinarService.deleteWebinar(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/list")
    ResponseEntity<BaseResponse<List<WebinarResponse>>> getWebinarList() {
        BaseResponse<List<WebinarResponse>> response = webinarService.getWebinarList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/consumer/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> consumerList() {
        BaseResponse<List<UserResponse>> response = userService.getUserList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/course/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createCourse(
            @RequestBody @Validated CreateUpdateCourseRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = courseService.createCourse(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/availability")
    ResponseEntity<BaseResponse<Boolean>> webinarAvailability(
            @RequestParam @Validated String webinarSecureId,
            @RequestParam @Validated boolean isAvailable
    ) {
        BaseResponse<Boolean> response = webinarService.setAvailability(webinarSecureId, isAvailable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/course/availability")
    ResponseEntity<BaseResponse<Boolean>> courseAvailability(
            @RequestParam @Validated String courseSecureId,
            @RequestParam @Validated boolean isAvailable
    ) {
        BaseResponse<Boolean> response = courseService.setAvailability(courseSecureId, isAvailable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
