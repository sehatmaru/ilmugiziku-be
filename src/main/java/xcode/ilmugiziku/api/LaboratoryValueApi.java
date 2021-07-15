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
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.presenter.LaboratoryPresenter;
import xcode.ilmugiziku.presenter.SchedulePresenter;

import java.util.List;

@RestController
@RequestMapping(value = "laboratory-value")
public class LaboratoryValueApi {

    final LaboratoryPresenter laboratoryPresenter;

    public LaboratoryValueApi(LaboratoryPresenter laboratoryPresenter) {
        this.laboratoryPresenter = laboratoryPresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<LaboratoryValueResponse>>> list () {
        BaseResponse<List<LaboratoryValueResponse>> response = laboratoryPresenter.getLaboratoryValueList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
