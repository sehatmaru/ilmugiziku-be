package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.TheoryResponse;
import xcode.ilmugiziku.presenter.TheoryPresenter;

import java.util.List;

@RestController
@RequestMapping(value = "theory")
public class TheoryApi {

    final TheoryPresenter theoryPresenter;

    public TheoryApi(TheoryPresenter theoryPresenter) {
        this.theoryPresenter = theoryPresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<TheoryResponse>>> list (@RequestHeader @Validated String token, @RequestParam @Validated int theoryType) {
        BaseResponse<List<TheoryResponse>> response = theoryPresenter.getTheoryList(token, theoryType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
