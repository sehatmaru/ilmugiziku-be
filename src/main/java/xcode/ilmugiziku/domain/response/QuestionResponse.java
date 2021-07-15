package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private String secureId;
    private String content;
    private int questionType;
    private int questionSubType;
    private List<AnswerResponse> answers;
}
