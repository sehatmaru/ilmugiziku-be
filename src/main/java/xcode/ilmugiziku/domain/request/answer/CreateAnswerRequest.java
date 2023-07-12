package xcode.ilmugiziku.domain.request.answer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateAnswerRequest {

    @NotBlank()
    private String content;
    private boolean value;

    public CreateAnswerRequest() {
    }
}
