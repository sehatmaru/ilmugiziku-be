package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.response.answer.AnswerValueResponse;

import java.util.List;

@Getter
@Setter
public class QuestionAnswerResponse {
    private String secureId;
    private String content;
    private String discussion;
    private String label;
    private List<AnswerValueResponse> answers;
}
