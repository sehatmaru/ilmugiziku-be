package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateExamResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.question.QuestionValueResponse;
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
    ResponseEntity<BaseResponse<List<QuestionValueResponse>>> quizList (@RequestParam @Validated String token, @RequestParam @Validated int questionType) {
        BaseResponse<List<QuestionValueResponse>> response = new BaseResponse<>();

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
    ResponseEntity<BaseResponse<List<QuestionResponse>>> tryOutList (@RequestParam @Validated String token, @RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
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
}
