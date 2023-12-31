package xcode.marsiajar.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.marsiajar.domain.request.exam.ExamResultRequest;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.exam.DoExamResponse;
import xcode.marsiajar.domain.response.exam.ExamListResponse;
import xcode.marsiajar.domain.response.exam.ExamRankResponse;
import xcode.marsiajar.domain.response.exam.ExamResultResponse;
import xcode.marsiajar.service.ExamService;

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

    @GetMapping("/cancel")
    ResponseEntity<BaseResponse<Boolean>> cancel (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<Boolean> response = examService.cancel(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<ExamListResponse>>> list(
            @RequestParam String title,
            @RequestParam String categorySecureId
    ) {
        BaseResponse<List<ExamListResponse>> response = examService.getExamList(title, categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
