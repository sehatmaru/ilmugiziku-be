package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.exam.CreateExamResponse;
import xcode.ilmugiziku.domain.response.exam.ExamKeyResponse;
import xcode.ilmugiziku.domain.response.exam.ExamRankResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;
import xcode.ilmugiziku.presenter.ExamPresenter;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.PRACTICE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

@RestController
@RequestMapping(value = "exam")
public class ExamApi {

    final QuestionPresenter questionPresenter;
    final ExamPresenter examPresenter;

    public ExamApi(QuestionPresenter questionPresenter, ExamPresenter examPresenter) {
        this.questionPresenter = questionPresenter;
        this.examPresenter = examPresenter;
    }

    @GetMapping("/quiz/list")
    ResponseEntity<BaseResponse<List<QuestionAnswerResponse>>> getQuizList (@RequestParam @Validated String token, @RequestParam @Validated int questionType) {
        BaseResponse<List<QuestionAnswerResponse>> response = new BaseResponse<>();

        if (questionType == QUIZ) {
            response = questionPresenter.getQuizQuestions(token);
        } else if (questionType == PRACTICE) {
            response = questionPresenter.getPracticeQuestions(token);
        } else {
            response.setWrongParams();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/try-out/list")
    ResponseEntity<BaseResponse<List<QuestionResponse>>> getTryOutList (@RequestParam @Validated String token, @RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
        BaseResponse<List<QuestionResponse>> response = questionPresenter.getTryOutQuestion(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/submit")
    ResponseEntity<BaseResponse<CreateExamResponse>> submitExam (@RequestParam @Validated String token, @RequestBody @Validated CreateExamRequest request) {
        BaseResponse<CreateExamResponse> response = examPresenter.submitExam(token, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/result")
    ResponseEntity<BaseResponse<List<ExamResultResponse>>> getExamResult (@RequestParam @Validated String token, @RequestParam @Validated int questionType) {
        BaseResponse<List<ExamResultResponse>> response = examPresenter.getExamResult(token, questionType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/rank")
    ResponseEntity<BaseResponse<List<ExamRankResponse>>> getExamRank (@RequestParam @Validated String token, @RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
        BaseResponse<List<ExamRankResponse>> response = examPresenter.getExamRank(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/key")
    ResponseEntity<BaseResponse<List<ExamKeyResponse>>> getExamKey (@RequestParam @Validated String token, @RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
        BaseResponse<List<ExamKeyResponse>> response = examPresenter.getExamKey(token, questionType, questionSubType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
