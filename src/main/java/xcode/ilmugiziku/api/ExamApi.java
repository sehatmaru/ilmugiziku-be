package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.exam.DoExamResponse;
import xcode.ilmugiziku.service.ExamService;

@RestController
@RequestMapping(value = "exam")
public class ExamApi {

    @Autowired private ExamService examService;

    @PostMapping("/apply")
    ResponseEntity<BaseResponse<Boolean>> apply (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<Boolean> response = examService.apply(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/do")
    ResponseEntity<BaseResponse<DoExamResponse>> doExam (
            @RequestParam @Validated String examSecureId
    ) {
        BaseResponse<DoExamResponse> response = examService.doExam(examSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
