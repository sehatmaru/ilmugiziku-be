package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.laboratory.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.laboratory.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.lesson.CreateLessonRequest;
import xcode.ilmugiziku.domain.request.lesson.UpdateLessonRequest;
import xcode.ilmugiziku.domain.request.pack.UpdatePackageRequest;
import xcode.ilmugiziku.domain.request.packagefeature.CreatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.packagefeature.UpdatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.request.template.CreateTemplateRequest;
import xcode.ilmugiziku.domain.request.template.UpdateTemplateRequest;
import xcode.ilmugiziku.domain.request.theory.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.theory.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.webinar.CreateWebinarRequest;
import xcode.ilmugiziku.domain.request.webinar.UpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.auth.UserResponse;
import xcode.ilmugiziku.service.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    @Autowired private QuestionService questionService;
    @Autowired private ScheduleService scheduleService;
    @Autowired private LaboratoryValueService laboratoryService;
    @Autowired private TheoryService theoryService;
    @Autowired private AuthService authService;
    @Autowired private DiscussionVideoService discussionVideoService;
    @Autowired private TemplateService templateService;
    @Autowired private PackageFeatureService packageFeatureService;
    @Autowired private PackageService packageService;
    @Autowired private WebinarService webinarService;
    @Autowired private LessonService lessonService;

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateQuestionRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = questionService.createQuestion(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/question/update")
    ResponseEntity<BaseResponse<Boolean>> updateQuestion(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateQuestionRequest body
    ) {
        BaseResponse<Boolean> response = questionService.updateQuestion(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/question/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteQuestion(
            @RequestParam @Validated String token,
            @RequestParam @Validated String questionSecureId
    ) {
        BaseResponse<Boolean> response = questionService.deleteQuestion(token, questionSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/schedule/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createSchedule(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateScheduleRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = scheduleService.createSchedule(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/schedule/update")
    ResponseEntity<BaseResponse<Boolean>> updateSchedule(
            @RequestParam @Validated String token,
            @RequestParam @Validated String scheduleSecureId,
            @RequestBody @Validated UpdateScheduleRequest body
    ) {
        BaseResponse<Boolean> response = scheduleService.updateSchedule(token, scheduleSecureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/schedule/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteSchedule(
            @RequestParam @Validated String token,
            @RequestParam @Validated String scheduleSecureId
    ) {
        BaseResponse<Boolean> response = scheduleService.deleteSchedule(token, scheduleSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/laboratory-value/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLaboratoryValue(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateLaboratoryValueRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = laboratoryService.createLaboratoryValue(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/laboratory-value/update")
    ResponseEntity<BaseResponse<Boolean>> updateLaboratoryValue(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateLaboratoryValueRequest body
    ) {
        BaseResponse<Boolean> response = laboratoryService.updateLaboratoryValue(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/laboratory-value/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLaboratoryValue(
            @RequestParam @Validated String token,
            @RequestParam @Validated String laboratoryValueSecureId
    ) {
        BaseResponse<Boolean> response = laboratoryService.deleteLaboratoryValue(token, laboratoryValueSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/theory/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTheory(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateTheoryRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = theoryService.createTheory(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/theory/update")
    ResponseEntity<BaseResponse<Boolean>> updateTheory(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateTheoryRequest body
    ) {
        BaseResponse<Boolean> response = theoryService.updateTheory(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/theory/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTheory(
            @RequestParam @Validated String token,
            @RequestParam @Validated String theorySecureId
    ) {
        BaseResponse<Boolean> response = theoryService.deleteTheory(token, theorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/discussion-video/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateDiscussionVideoRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = discussionVideoService.createDiscussionVideo(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/discussion-video/update")
    ResponseEntity<BaseResponse<Boolean>> updateDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateDiscussionVideoRequest body
    ) {
        BaseResponse<Boolean> response = discussionVideoService.updateDiscussionVideo(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/discussion-video/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId) {
        BaseResponse<Boolean> response = discussionVideoService.deleteDiscussionVideo(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/discussion-video")
    ResponseEntity<BaseResponse<DiscussionVideoResponse>> getDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestParam @Validated String templateSecureId) {
        BaseResponse<DiscussionVideoResponse> response = discussionVideoService.getDiscussionVideo(token, templateSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/template-to/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTemplateTo(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateTemplateRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = templateService.createTemplate(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/template-to/update")
    ResponseEntity<BaseResponse<Boolean>> updateTemplateTo(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateTemplateRequest body
    ) {
        BaseResponse<Boolean> response = templateService.updateTemplate(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/template-to/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTemplateTo(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = templateService.deleteTemplate(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/template-to/list")
    ResponseEntity<BaseResponse<List<TemplateResponse>>> getTemplateToList(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType,
            @RequestParam @Validated int questionSubType
    ) {
        BaseResponse<List<TemplateResponse>> response = templateService.getTemplateList(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/template-to/set")
    ResponseEntity<BaseResponse<Boolean>> setTemplateToActive(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = templateService.setTemplateActive(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/package-feature/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createPackageFeature(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreatePackageFeatureRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = packageFeatureService.createPackageFeature(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/package-feature/update")
    ResponseEntity<BaseResponse<Boolean>> updatePackageFeature(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdatePackageFeatureRequest body
    ) {
        BaseResponse<Boolean> response = packageFeatureService.updatePackageFeature(token, secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/package-feature/delete")
    ResponseEntity<BaseResponse<Boolean>> deletePackageFeature(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = packageFeatureService.deletePackageFeature(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package-feature/list")
    ResponseEntity<BaseResponse<List<PackageFeatureResponse>>> getPackageFeatureList(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<PackageFeatureResponse>> response = packageFeatureService.getPackageFeatureList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/package/update")
    ResponseEntity<BaseResponse<Boolean>> updatePackage(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdatePackageRequest body
    ) {
        BaseResponse<Boolean> response = packageService.updatePackage(token, secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createWebinar(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateWebinarRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = webinarService.createWebinar(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/webinar/update")
    ResponseEntity<BaseResponse<Boolean>> updateWebinar(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdateWebinarRequest body
    ) {
        BaseResponse<Boolean> response = webinarService.updateWebinar(token, secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/webinar/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteWebinar(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = webinarService.deleteWebinar(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/list")
    ResponseEntity<BaseResponse<List<WebinarResponse>>> getWebinarList(
            @RequestParam @Validated String token,
            @RequestParam @Validated int bimbelType
    ) {
        BaseResponse<List<WebinarResponse>> response = webinarService.getWebinarList(token, bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/lesson/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLesson(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateLessonRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = lessonService.createLesson(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/lesson/update")
    ResponseEntity<BaseResponse<Boolean>> updateLesson(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdateLessonRequest body
    ) {
        BaseResponse<Boolean> response = lessonService.updateLesson(token, secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/lesson/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLesson(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = lessonService.deleteLesson(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/lesson/list")
    ResponseEntity<BaseResponse<List<LessonResponse>>> getLessonList(
            @RequestParam @Validated String token,
            @RequestParam @Validated int bimbelType
    ) {
        BaseResponse<List<LessonResponse>> response = lessonService.getLessonList(token, bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/consumer/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> consumerList(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<UserResponse>> response = authService.getUserList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/token/destroy")
    ResponseEntity<BaseResponse<Boolean>> destroyToken(
            @RequestParam @Validated @NotBlank String email
    ) {
        BaseResponse<Boolean> response = authService.destroyToken(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
