package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.enums.BimbelTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;
import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
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
import xcode.ilmugiziku.domain.request.webinar.CreateWebinarRequest;
import xcode.ilmugiziku.domain.request.webinar.UpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.user.UserResponse;
import xcode.ilmugiziku.service.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    @Autowired private QuestionService questionService;
    @Autowired private ScheduleService scheduleService;
    @Autowired private LaboratoryValueService laboratoryService;
    @Autowired private TheoryService theoryService;
    @Autowired private UserService userService;
    @Autowired private DiscussionVideoService discussionVideoService;
    @Autowired private TemplateService templateService;
    @Autowired private PackageFeatureService packageFeatureService;
    @Autowired private PackageService packageService;
    @Autowired private WebinarService webinarService;
    @Autowired private LessonService lessonService;

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (
            @RequestBody @Validated CreateQuestionRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = questionService.createQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/question/update")
    ResponseEntity<BaseResponse<Boolean>> updateQuestion(
            @RequestBody @Validated UpdateQuestionRequest body
    ) {
        BaseResponse<Boolean> response = questionService.updateQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

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

    @PostMapping("/schedule/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createSchedule(
            @RequestBody @Validated CreateScheduleRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = scheduleService.createSchedule(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/schedule/update")
    ResponseEntity<BaseResponse<Boolean>> updateSchedule(
            @RequestParam @Validated String scheduleSecureId,
            @RequestBody @Validated UpdateScheduleRequest body
    ) {
        BaseResponse<Boolean> response = scheduleService.updateSchedule(scheduleSecureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/schedule/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteSchedule(
            @RequestParam @Validated String scheduleSecureId
    ) {
        BaseResponse<Boolean> response = scheduleService.deleteSchedule(scheduleSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/laboratory-value/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLaboratoryValue(
            @RequestBody @Validated CreateLaboratoryValueRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = laboratoryService.createLaboratoryValue(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/laboratory-value/update")
    ResponseEntity<BaseResponse<Boolean>> updateLaboratoryValue(
            @RequestBody @Validated UpdateLaboratoryValueRequest body
    ) {
        BaseResponse<Boolean> response = laboratoryService.updateLaboratoryValue(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/laboratory-value/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLaboratoryValue(
            @RequestParam @Validated String laboratoryValueSecureId
    ) {
        BaseResponse<Boolean> response = laboratoryService.deleteLaboratoryValue(laboratoryValueSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/theory/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTheory(
            @RequestBody @Validated CreateTheoryRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = theoryService.createTheory(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/theory/update")
    ResponseEntity<BaseResponse<Boolean>> updateTheory(
            @RequestBody @Validated UpdateTheoryRequest body
    ) {
        BaseResponse<Boolean> response = theoryService.updateTheory(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/theory/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTheory(
            @RequestParam @Validated String theorySecureId
    ) {
        BaseResponse<Boolean> response = theoryService.deleteTheory(theorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/discussion-video/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createDiscussionVideo(
            @RequestBody @Validated CreateDiscussionVideoRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = discussionVideoService.createDiscussionVideo(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/discussion-video/update")
    ResponseEntity<BaseResponse<Boolean>> updateDiscussionVideo(
            @RequestBody @Validated UpdateDiscussionVideoRequest body
    ) {
        BaseResponse<Boolean> response = discussionVideoService.updateDiscussionVideo(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/discussion-video/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteDiscussionVideo(
            @RequestParam @Validated String secureId) {
        BaseResponse<Boolean> response = discussionVideoService.deleteDiscussionVideo(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/discussion-video")
    ResponseEntity<BaseResponse<DiscussionVideoResponse>> getDiscussionVideo(
            @RequestParam @Validated String templateSecureId) {
        BaseResponse<DiscussionVideoResponse> response = discussionVideoService.getDiscussionVideo(templateSecureId);

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

    @GetMapping("/template-to/list")
    ResponseEntity<BaseResponse<List<TemplateResponse>>> getTemplateToList(
            @RequestParam @Validated QuestionTypeEnum questionType,
            @RequestParam @Validated QuestionSubTypeEnum questionSubType
    ) {
        BaseResponse<List<TemplateResponse>> response = templateService.getTemplateList(questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/template-to/set")
    ResponseEntity<BaseResponse<Boolean>> setTemplateToActive(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = templateService.setTemplateActive(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/package-feature/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createPackageFeature(
            @RequestBody @Validated CreatePackageFeatureRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = packageFeatureService.createPackageFeature(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/package-feature/update")
    ResponseEntity<BaseResponse<Boolean>> updatePackageFeature(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdatePackageFeatureRequest body
    ) {
        BaseResponse<Boolean> response = packageFeatureService.updatePackageFeature(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/package-feature/delete")
    ResponseEntity<BaseResponse<Boolean>> deletePackageFeature(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = packageFeatureService.deletePackageFeature(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package-feature/list")
    ResponseEntity<BaseResponse<List<PackageFeatureResponse>>> getPackageFeatureList(
    ) {
        BaseResponse<List<PackageFeatureResponse>> response = packageFeatureService.getPackageFeatureList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/package/update")
    ResponseEntity<BaseResponse<Boolean>> updatePackage(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdatePackageRequest body
    ) {
        BaseResponse<Boolean> response = packageService.updatePackage(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createWebinar(
            @RequestBody @Validated CreateWebinarRequest body
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
            @RequestBody @Validated UpdateWebinarRequest body
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
    ResponseEntity<BaseResponse<List<WebinarResponse>>> getWebinarList(
            @RequestParam @Validated BimbelTypeEnum bimbelType
    ) {
        BaseResponse<List<WebinarResponse>> response = webinarService.getWebinarList(bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/lesson/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLesson(
            @RequestBody @Validated CreateLessonRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = lessonService.createLesson(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/lesson/update")
    ResponseEntity<BaseResponse<Boolean>> updateLesson(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated UpdateLessonRequest body
    ) {
        BaseResponse<Boolean> response = lessonService.updateLesson(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/lesson/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLesson(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = lessonService.deleteLesson(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/lesson/list")
    ResponseEntity<BaseResponse<List<LessonResponse>>> getLessonList(
            @RequestParam @Validated BimbelTypeEnum bimbelType
    ) {
        BaseResponse<List<LessonResponse>> response = lessonService.getLessonList(bimbelType);

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
}
