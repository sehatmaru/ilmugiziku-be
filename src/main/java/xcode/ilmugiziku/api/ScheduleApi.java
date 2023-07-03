package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
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
import xcode.ilmugiziku.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping(value = "schedule")
public class ScheduleApi {

    @Autowired private ScheduleService scheduleService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<ScheduleResponse>>> list (@RequestParam @Validated String token) {
        BaseResponse<List<ScheduleResponse>> response = scheduleService.getScheduleList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/check")
    ResponseEntity<BaseResponse<Boolean>> check (@RequestParam @Validated String token) {
        BaseResponse<Boolean> response = scheduleService.checkSchedule(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
