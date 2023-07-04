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
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.TheoryResponse;
import xcode.ilmugiziku.service.TheoryService;

import java.util.List;

@RestController
@RequestMapping(value = "theory")
public class TheoryApi {

    @Autowired private TheoryService theoryService;

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<TheoryResponse>>> list (@RequestParam @Validated TheoryTypeEnum theoryType) {
        BaseResponse<List<TheoryResponse>> response = theoryService.getTheoryList(theoryType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
