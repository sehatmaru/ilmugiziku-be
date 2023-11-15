package xcode.marsiajar.domain.response.answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponse {
    private String secureId;
    private String content;
    private boolean correctAnswer;
}
