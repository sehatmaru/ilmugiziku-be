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
    ResponseEntity<BaseResponse<List<ScheduleResponse>>> list (@RequestParam @Validated String token) {
        BaseResponse<List<ScheduleResponse>> response = schedulePresenter.getScheduleList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
