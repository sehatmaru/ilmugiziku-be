package xcode.marsiajar.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ExamRequest {
    @NotBlank()
    private String questionsSecureId;
    @NotBlank()
    private String answersSecureId;

    public ExamRequest() {
    }
}
