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
    ResponseEntity<BaseResponse<List<TheoryResponse>>> list (@RequestParam @Validated String token, @RequestParam @Validated int theoryType) {
        BaseResponse<List<TheoryResponse>> response = theoryPresenter.getTheoryList(token, theoryType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
