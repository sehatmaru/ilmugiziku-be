package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.institution.CreateInstituteRequest;
import xcode.ilmugiziku.domain.request.institution.UpdateInstituteRequest;
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
import xcode.ilmugiziku.presenter.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    final QuestionPresenter questionPresenter;
    final SchedulePresenter schedulePresenter;
    final LaboratoryPresenter laboratoryPresenter;
    final TheoryPresenter theoryPresenter;
    final InstitutePresenter institutePresenter;
    final AuthPresenter authPresenter;
    final DiscussionVideoPresenter discussionVideoPresenter;
    final TemplatePresenter templatePresenter;
    final PackageFeaturePresenter packageFeaturePresenter;
    final PackagePresenter packagePresenter;
    final WebinarPresenter webinarPresenter;
    final LessonPresenter lessonPresenter;

    public AdminApi(QuestionPresenter questionPresenter,
                    SchedulePresenter schedulePresenter,
                    LaboratoryPresenter laboratoryPresenter,
                    TheoryPresenter theoryPresenter,
                    InstitutePresenter institutePresenter,
                    AuthPresenter authPresenter,
                    DiscussionVideoPresenter discussionVideoPresenter,
                    TemplatePresenter templatePresenter,
                    PackageFeaturePresenter packageFeaturePresenter,
                    PackagePresenter packagePresenter,
                    WebinarPresenter webinarPresenter,
                    LessonPresenter lessonPresenter) {
        this.questionPresenter = questionPresenter;
        this.schedulePresenter = schedulePresenter;
        this.laboratoryPresenter = laboratoryPresenter;
        this.theoryPresenter = theoryPresenter;
        this.institutePresenter = institutePresenter;
        this.authPresenter = authPresenter;
        this.discussionVideoPresenter = discussionVideoPresenter;
        this.templatePresenter = templatePresenter;
        this.packageFeaturePresenter = packageFeaturePresenter;
        this.packagePresenter = packagePresenter;
        this.webinarPresenter = webinarPresenter;
        this.lessonPresenter = lessonPresenter;
    }

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateQuestionRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = questionPresenter.createQuestion(token, body);

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
        BaseResponse<Boolean> response = questionPresenter.updateQuestion(token, body);

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
        BaseResponse<Boolean> response = questionPresenter.deleteQuestion(token, questionSecureId);

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
        BaseResponse<CreateBaseResponse> response = schedulePresenter.createSchedule(token, body);

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
        BaseResponse<Boolean> response = schedulePresenter.updateSchedule(token, scheduleSecureId, body);

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
        BaseResponse<Boolean> response = schedulePresenter.deleteSchedule(token, scheduleSecureId);

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
        BaseResponse<CreateBaseResponse> response = laboratoryPresenter.createLaboratoryValue(token, body);

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
        BaseResponse<Boolean> response = laboratoryPresenter.updateLaboratoryValue(token, body);

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
        BaseResponse<Boolean> response = laboratoryPresenter.deleteLaboratoryValue(token, laboratoryValueSecureId);

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
        BaseResponse<CreateBaseResponse> response = theoryPresenter.createTheory(token, body);

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
        BaseResponse<Boolean> response = theoryPresenter.updateTheory(token, body);

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
        BaseResponse<Boolean> response = theoryPresenter.deleteTheory(token, theorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/institute/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createInstitute(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateInstituteRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = institutePresenter.createInstitute(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/institute/update")
    ResponseEntity<BaseResponse<Boolean>> updateInstitute(
            @RequestParam @Validated String token,
            @RequestBody @Validated UpdateInstituteRequest body
    ) {
        BaseResponse<Boolean> response = institutePresenter.updateInstitute(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/institute/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteInstitute(
            @RequestParam @Validated String token,
            @RequestParam @Validated String instituteSecureId
    ) {
        BaseResponse<Boolean> response = institutePresenter.deleteInstitute(token, instituteSecureId);

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
        BaseResponse<CreateBaseResponse> response = discussionVideoPresenter.createDiscussionVideo(token, body);

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
        BaseResponse<Boolean> response = discussionVideoPresenter.updateDiscussionVideo(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/discussion-video/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestParam @Validated String secureId) {
        BaseResponse<Boolean> response = discussionVideoPresenter.deleteDiscussionVideo(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/discussion-video")
    ResponseEntity<BaseResponse<DiscussionVideoResponse>> getDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestParam @Validated String templateSecureId) {
        BaseResponse<DiscussionVideoResponse> response = discussionVideoPresenter.getDiscussionVideo(token, templateSecureId);

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
        BaseResponse<CreateBaseResponse> response = templatePresenter.createTemplate(token, body);

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
        BaseResponse<Boolean> response = templatePresenter.updateTemplate(token, body);

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
        BaseResponse<Boolean> response = templatePresenter.deleteTemplate(token, secureId);

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
        BaseResponse<List<TemplateResponse>> response = templatePresenter.getTemplateList(token, questionType, questionSubType);

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
        BaseResponse<Boolean> response = templatePresenter.setTemplateActive(token, secureId);

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
        BaseResponse<CreateBaseResponse> response = packageFeaturePresenter.createPackageFeature(token, body);

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
        BaseResponse<Boolean> response = packageFeaturePresenter.updatePackageFeature(token, secureId, body);

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
        BaseResponse<Boolean> response = packageFeaturePresenter.deletePackageFeature(token, secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package-feature/list")
    ResponseEntity<BaseResponse<List<PackageFeatureResponse>>> getPackageFeatureList(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<PackageFeatureResponse>> response = packageFeaturePresenter.getPackageFeatureList(token);

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
        BaseResponse<Boolean> response = packagePresenter.updatePackage(token, secureId, body);

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
        BaseResponse<CreateBaseResponse> response = webinarPresenter.createWebinar(token, body);

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
        BaseResponse<Boolean> response = webinarPresenter.updateWebinar(token, secureId, body);

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
        BaseResponse<Boolean> response = webinarPresenter.deleteWebinar(token, secureId);

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
        BaseResponse<List<WebinarResponse>> response = webinarPresenter.getWebinarList(token, bimbelType);

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
        BaseResponse<CreateBaseResponse> response = lessonPresenter.createLesson(token, body);

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
        BaseResponse<Boolean> response = lessonPresenter.updateLesson(token, secureId, body);

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
        BaseResponse<Boolean> response = lessonPresenter.deleteLesson(token, secureId);

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
        BaseResponse<List<LessonResponse>> response = lessonPresenter.getLessonList(token, bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/consumer/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> consumerList(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<UserResponse>> response = authPresenter.getUserList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/token/destroy")
    ResponseEntity<BaseResponse<Boolean>> destroyToken(
            @RequestParam @Validated String email
    ) {
        BaseResponse<Boolean> response = authPresenter.destroyToken(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
