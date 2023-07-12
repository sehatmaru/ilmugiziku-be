package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateUpdateAnswerRequest {

    @NotBlank()
    private String content;

    private boolean correctAnswer;

    public CreateUpdateAnswerRequest() {
    }

}
