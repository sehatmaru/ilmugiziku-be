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
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.service.LaboratoryValueService;

import java.util.List;

@RestController
@RequestMapping(value = "laboratory-value")
public class LaboratoryValueApi {

    @Autowired private LaboratoryValueService laboratoryService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<LaboratoryValueResponse>>> list (@RequestParam @Validated String token) {
        BaseResponse<List<LaboratoryValueResponse>> response = laboratoryService.getLaboratoryValueList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
