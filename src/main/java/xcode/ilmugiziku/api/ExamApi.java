package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.exam.*;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;
import xcode.ilmugiziku.service.ExamService;
import xcode.ilmugiziku.service.QuestionService;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.PRACTICE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

@RestController
@RequestMapping(value = "exam")
public class ExamApi {

    @Autowired private QuestionService questionService;
    @Autowired private ExamService examService;

    @GetMapping("/quiz/list")
    ResponseEntity<BaseResponse<List<QuestionAnswerResponse>>> getQuizList(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType
    ) {
        BaseResponse<List<QuestionAnswerResponse>> response = new BaseResponse<>();

        if (questionType == QUIZ) {
            response = questionService.getQuizQuestions(token);
        } else if (questionType == PRACTICE) {
            response = questionService.getPracticeQuestions(token);
        } else {
            response.setWrongParams();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/try-out/list")
    ResponseEntity<BaseResponse<QuestionResponse>> getTryOutList (
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType,
            @RequestParam @Validated int questionSubType,
            @RequestParam String templateSecureId
    ) {
        BaseResponse<QuestionResponse> response = questionService.getTryOutQuestion(token, questionType, questionSubType, templateSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/submit")
    ResponseEntity<BaseResponse<CreateExamResponse>> submitExam(
            @RequestParam @Validated String token,
            @RequestBody @Validated CreateExamRequest request
    ) {
        BaseResponse<CreateExamResponse> response = examService.submitExam(token, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/result")
    ResponseEntity<BaseResponse<List<ExamResultResponse>>> getExamResult(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType
    ) {
        BaseResponse<List<ExamResultResponse>> response = examService.getExamResult(token, questionType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/rank")
    ResponseEntity<BaseResponse<List<ExamRankResponse>>> getExamRank(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType,
            @RequestParam @Validated int questionSubType
    ) {
        BaseResponse<List<ExamRankResponse>> response = examService.getExamRank(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/key")
    ResponseEntity<BaseResponse<List<ExamKeyResponse>>> getExamKey(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType,
            @RequestParam @Validated int questionSubType
    ) {
        BaseResponse<List<ExamKeyResponse>> response = examService.getExamKey(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/information")
    ResponseEntity<BaseResponse<List<ExamInformationResponse>>> getExamInformation(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType
    ) {
        BaseResponse<List<ExamInformationResponse>> response = examService.getExamInformation(token, questionType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/discussion-video")
    ResponseEntity<BaseResponse<List<ExamVideoResponse>>> getExamDiscussionVideo(
            @RequestParam @Validated String token,
            @RequestParam @Validated int questionType
    ) {
        BaseResponse<List<ExamVideoResponse>> response = examService.getExamVideo(token, questionType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
