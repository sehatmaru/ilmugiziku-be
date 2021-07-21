package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.response.answer.AnswerValueResponse;

import java.util.List;

@Getter
@Setter
public class QuestionValueResponse {
    private String secureId;
    private String content;
    private int questionType;
    private int questionSubType;
    private List<AnswerValueResponse> answers;
}
