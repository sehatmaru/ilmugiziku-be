package xcode.ilmugiziku.domain.request.answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnswerRequest {
    private String content;
    private boolean value;

    public CreateAnswerRequest() {
    }
}
