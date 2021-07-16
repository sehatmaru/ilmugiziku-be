package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@RestController
@RequestMapping(value = "question")
public class QuestionApi {

    final QuestionPresenter questionPresenter;

    public QuestionApi(QuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<QuestionResponse>>> questionList (@RequestHeader @Validated String token, @RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
        BaseResponse<List<QuestionResponse>> response;

        if (questionType == QUIZ) {
            response = questionPresenter.getQuizQuestions(token);
        } else if (questionType == PRACTICE) {
            response = questionPresenter.getPracticeQuestions(token);
        } else {
            response = questionPresenter.getTryOutQuestion(token, questionType, questionSubType);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
