package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExamResponse {
    private String secureId;
    private int score;
    private int falseCount;
    private int correctCount;
}
