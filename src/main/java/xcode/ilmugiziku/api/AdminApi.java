package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.institution.CreateInstituteRequest;
import xcode.ilmugiziku.domain.request.institution.UpdateInstituteRequest;
import xcode.ilmugiziku.domain.request.laboratory.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.laboratory.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.request.theory.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.theory.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.request.video.CreateVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
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
    final VideoPresenter videoPresenter;

    public AdminApi(QuestionPresenter questionPresenter,
                    SchedulePresenter schedulePresenter,
                    LaboratoryPresenter laboratoryPresenter,
                    TheoryPresenter theoryPresenter,
                    InstitutePresenter institutePresenter,
                    AuthPresenter authPresenter,
                    VideoPresenter videoPresenter) {
        this.questionPresenter = questionPresenter;
        this.schedulePresenter = schedulePresenter;
        this.laboratoryPresenter = laboratoryPresenter;
        this.theoryPresenter = theoryPresenter;
        this.institutePresenter = institutePresenter;
        this.authPresenter = authPresenter;
        this.videoPresenter = videoPresenter;
    }

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (@RequestParam @Validated String token, @RequestBody @Validated CreateQuestionRequest body) {
        BaseResponse<CreateBaseResponse> response = questionPresenter.createQuestion(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/question/update")
    ResponseEntity<BaseResponse<Boolean>> updateQuestion(@RequestParam @Validated String token, @RequestBody @Validated UpdateQuestionRequest body) {
        BaseResponse<Boolean> response = questionPresenter.updateQuestion(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/question/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteQuestion(@RequestParam @Validated String token, @RequestParam @Validated String questionSecureId) {
        BaseResponse<Boolean> response = questionPresenter.deleteQuestion(token, questionSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/schedule/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createSchedule (@RequestParam @Validated String token, @RequestBody @Validated CreateScheduleRequest body) {
        BaseResponse<CreateBaseResponse> response = schedulePresenter.createSchedule(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/schedule/update")
    ResponseEntity<BaseResponse<Boolean>> updateSchedule(@RequestParam @Validated String token, @RequestParam @Validated String scheduleSecureId, @RequestBody @Validated UpdateScheduleRequest body) {
        BaseResponse<Boolean> response = schedulePresenter.updateSchedule(token, scheduleSecureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/schedule/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteSchedule(@RequestParam @Validated String token, @RequestParam @Validated String scheduleSecureId) {
        BaseResponse<Boolean> response = schedulePresenter.deleteSchedule(token, scheduleSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/laboratory-value/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLaboratoryValue (@RequestParam @Validated String token, @RequestBody @Validated CreateLaboratoryValueRequest body) {
        BaseResponse<CreateBaseResponse> response = laboratoryPresenter.createLaboratoryValue(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/laboratory-value/update")
    ResponseEntity<BaseResponse<Boolean>> updateLaboratoryValue(@RequestParam @Validated String token, @RequestBody @Validated UpdateLaboratoryValueRequest body) {
        BaseResponse<Boolean> response = laboratoryPresenter.updateLaboratoryValue(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/laboratory-value/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLaboratoryValue(@RequestParam @Validated String token, @RequestParam @Validated String laboratoryValueSecureId) {
        BaseResponse<Boolean> response = laboratoryPresenter.deleteLaboratoryValue(token, laboratoryValueSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/theory/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTheory (@RequestParam @Validated String token, @RequestBody @Validated CreateTheoryRequest body) {
        BaseResponse<CreateBaseResponse> response = theoryPresenter.createTheory(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/theory/update")
    ResponseEntity<BaseResponse<Boolean>> updateTheory(@RequestParam @Validated String token, @RequestBody @Validated UpdateTheoryRequest body) {
        BaseResponse<Boolean> response = theoryPresenter.updateTheory(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/theory/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTheory(@RequestParam @Validated String token, @RequestParam @Validated String theorySecureId) {
        BaseResponse<Boolean> response = theoryPresenter.deleteTheory(token, theorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/institute/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createInstitute (@RequestParam @Validated String token, @RequestBody @Validated CreateInstituteRequest body) {
        BaseResponse<CreateBaseResponse> response = institutePresenter.createInstitute(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/institute/update")
    ResponseEntity<BaseResponse<Boolean>> updateInstitute(@RequestParam @Validated String token, @RequestBody @Validated UpdateInstituteRequest body) {
        BaseResponse<Boolean> response = institutePresenter.updateInstitute(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/institute/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteInstitute(@RequestParam @Validated String token, @RequestParam @Validated String instituteSecureId) {
        BaseResponse<Boolean> response = institutePresenter.deleteInstitute(token, instituteSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/discussion-video/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createDiscussionVideo (@RequestParam @Validated String token, @RequestBody @Validated CreateVideoRequest body) {
        BaseResponse<CreateBaseResponse> response = videoPresenter.createDiscussionVideo(token, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/consumer/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> consumerList(@RequestParam @Validated String token) {
        BaseResponse<List<UserResponse>> response = authPresenter.getUserList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/token/destroy")
    ResponseEntity<BaseResponse<Boolean>> destroyToken(@RequestParam @Validated String email) {
        BaseResponse<Boolean> response = authPresenter.destroyToken(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
