package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAnswerRequest {
    private String secureId;
    private String content;
    private boolean value;

    public UpdateAnswerRequest() {
    }
}