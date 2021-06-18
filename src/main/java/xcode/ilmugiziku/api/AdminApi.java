package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    final QuestionPresenter questionPresenter;

    public AdminApi(QuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
    }

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createQuestion (@RequestBody @Validated CreateQuestionRequest body) {
        BaseResponse<CreateBaseResponse> response = questionPresenter.createQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/question/update")
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
}
