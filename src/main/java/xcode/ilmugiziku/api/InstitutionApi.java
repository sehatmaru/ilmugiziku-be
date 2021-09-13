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
import xcode.ilmugiziku.domain.response.InstituteResponse;
import xcode.ilmugiziku.presenter.InstitutePresenter;

import java.util.List;

@RestController
@RequestMapping(value = "institution")
public class InstitutionApi {

    final InstitutePresenter institutePresenter;

    public InstitutionApi(InstitutePresenter institutePresenter) {
        this.institutePresenter = institutePresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<InstituteResponse>>> list (@RequestParam @Validated String token) {
        BaseResponse<List<InstituteResponse>> response = institutePresenter.getInstitutionList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
