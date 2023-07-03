package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private List<QuestionExamResponse> exam;
    private int timeLimit;
}
