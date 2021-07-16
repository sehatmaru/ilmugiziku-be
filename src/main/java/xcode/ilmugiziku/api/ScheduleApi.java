package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.presenter.SchedulePresenter;

import java.util.List;

@RestController
@RequestMapping(value = "schedule")
public class ScheduleApi {

    final SchedulePresenter schedulePresenter;

    public ScheduleApi(SchedulePresenter schedulePresenter) {
        this.schedulePresenter = schedulePresenter;
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<ScheduleResponse>>> list (@RequestHeader @Validated String token, @RequestParam @Validated String authSecureId) {
        BaseResponse<List<ScheduleResponse>> response = schedulePresenter.getScheduleList(token, authSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
