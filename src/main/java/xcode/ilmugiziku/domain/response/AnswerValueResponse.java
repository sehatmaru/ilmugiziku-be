package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerValueResponse {
    private String secureId;
    private String content;
    private boolean value;
}
