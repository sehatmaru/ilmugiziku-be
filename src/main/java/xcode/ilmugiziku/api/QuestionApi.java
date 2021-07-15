package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.List;
import java.util.Optional;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@RestController
@RequestMapping(value = "question")
public class QuestionApi {

    final QuestionPresenter questionPresenter;

    public QuestionApi(QuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<QuestionResponse>>> questionList (@RequestParam @Validated int questionType, @RequestParam @Validated int questionSubType) {
        BaseResponse<List<QuestionResponse>> response;

        if (questionType == QUIZ) {
            response = questionPresenter.getQuizQuestions();
        } else if (questionType == PRACTICE) {
            response = questionPresenter.getPracticeQuestions();
        } else {
            response = questionPresenter.getTryOutQuestion(questionType, questionSubType);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}