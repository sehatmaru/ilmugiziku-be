package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.presenter.LaboratoryPresenter;

import java.util.List;

@RestController
@RequestMapping(value = "laboratory-value")
public class LaboratoryValueApi {

    final LaboratoryPresenter laboratoryPresenter;

    public LaboratoryValueApi(LaboratoryPresenter laboratoryPresenter) {
        this.laboratoryPresenter = laboratoryPresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<LaboratoryValueResponse>>> list (@RequestHeader @Validated String token) {
        BaseResponse<List<LaboratoryValueResponse>> response = laboratoryPresenter.getLaboratoryValueList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
