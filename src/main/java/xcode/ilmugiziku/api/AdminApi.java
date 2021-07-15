package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.presenter.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    final QuestionPresenter questionPresenter;
    final SchedulePresenter schedulePresenter;
    final LaboratoryPresenter laboratoryPresenter;
    final TheoryPresenter theoryPresenter;
    final AuthPresenter authPresenter;

    public AdminApi(QuestionPresenter questionPresenter,
                    SchedulePresenter schedulePresenter,
                    LaboratoryPresenter laboratoryPresenter,
                    TheoryPresenter theoryPresenter,
                    AuthPresenter authPresenter) {
        this.questionPresenter = questionPresenter;
        this.schedulePresenter = schedulePresenter;
        this.laboratoryPresenter = laboratoryPresenter;
        this.theoryPresenter = theoryPresenter;
        this.authPresenter = authPresenter;
    }

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (@RequestBody @Validated CreateQuestionRequest body) {
        BaseResponse<CreateBaseResponse> response = questionPresenter.createQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/question/update")
    ResponseEntity<BaseResponse<Boolean>> updateQuestion(@RequestBody @Validated UpdateQuestionRequest body) {
        BaseResponse<Boolean> response = questionPresenter.updateQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/question/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteQuestion(@RequestParam @Validated String questionSecureId) {
        BaseResponse<Boolean> response = questionPresenter.deleteQuestion(questionSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/schedule/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createSchedule (@RequestBody @Validated CreateScheduleRequest body) {
        BaseResponse<CreateBaseResponse> response = schedulePresenter.createSchedule(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/schedule/update")
    ResponseEntity<BaseResponse<Boolean>> updateSchedule(@RequestBody @Validated UpdateScheduleRequest body) {
        BaseResponse<Boolean> response = schedulePresenter.updateSchedule(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/schedule/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteSchedule(@RequestParam @Validated String scheduleSecureId) {
        BaseResponse<Boolean> response = schedulePresenter.deleteSchedule(scheduleSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/laboratory-value/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createLaboratoryValue (@RequestBody @Validated CreateLaboratoryValueRequest body) {
        BaseResponse<CreateBaseResponse> response = laboratoryPresenter.createLaboratoryValue(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/laboratory-value/update")
    ResponseEntity<BaseResponse<Boolean>> updateLaboratoryValue(@RequestBody @Validated UpdateLaboratoryValueRequest body) {
        BaseResponse<Boolean> response = laboratoryPresenter.updateLaboratoryValue(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/laboratory-value/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteLaboratoryValue(@RequestParam @Validated String laboratoryValueSecureId) {
        BaseResponse<Boolean> response = laboratoryPresenter.deleteLaboratoryValue(laboratoryValueSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/theory/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTheory (@RequestBody @Validated CreateTheoryRequest body) {
        BaseResponse<CreateBaseResponse> response = theoryPresenter.createTheory(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/theory/update")
    ResponseEntity<BaseResponse<Boolean>> updateTheory(@RequestBody @Validated UpdateTheoryRequest body) {
        BaseResponse<Boolean> response = theoryPresenter.updateTheory(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/theory/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTheory(@RequestParam @Validated String theorySecureId) {
        BaseResponse<Boolean> response = theoryPresenter.deleteTheory(theorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/consumer/list")
    ResponseEntity<BaseResponse<List<LoginResponse>>> consumerList() {
        BaseResponse<List<LoginResponse>> response = authPresenter.getUserList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
