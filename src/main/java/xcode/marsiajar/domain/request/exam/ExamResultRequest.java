package xcode.marsiajar.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ExamResultRequest {

    @NotBlank()
    private String question;
    @NotBlank()
    private String answer;

    public ExamResultRequest() {
    }

}
