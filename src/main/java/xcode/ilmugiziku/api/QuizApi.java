package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuizPresenter;

import java.util.List;

@RestController
@RequestMapping(value = "quiz")
public class QuizApi {

    @Autowired
    QuizPresenter quizPresenter;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<QuestionResponse>>> getQuiz () {
        BaseResponse<List<QuestionResponse>> response = quizPresenter.getQuizQuestions();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
