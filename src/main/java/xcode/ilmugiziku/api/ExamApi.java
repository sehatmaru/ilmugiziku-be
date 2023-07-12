package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.exam.ExamResultRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.exam.DoExamResponse;
import xcode.ilmugiziku.domain.response.exam.ExamRankResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;
import xcode.ilmugiziku.service.ExamService;

import java.util.List;

@RestController
@RequestMapping(value = "exam")
public class ExamApi {

    @Autowired private ExamService examService;

    @PostMapping("/apply")
    ResponseEntity<BaseResponse<Boolean>> apply (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<Boolean> response = examService.apply(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/start")
    ResponseEntity<BaseResponse<DoExamResponse>> startExam (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<DoExamResponse> response = examService.startExam(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/finish")
    ResponseEntity<BaseResponse<ExamResultResponse>> finishExam (
            @RequestParam @Validated String examSecureId,
            @RequestBody @Validated List<ExamResultRequest> request
    ) {
        BaseResponse<ExamResultResponse> response = examService.finishExam(examSecureId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/result")
    ResponseEntity<BaseResponse<ExamResultResponse>> getExamResult (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<ExamResultResponse> response = examService.getExamResult(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/rank")
    ResponseEntity<BaseResponse<ExamRankResponse>> getExamRank (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<ExamRankResponse> response = examService.getExamRank(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
