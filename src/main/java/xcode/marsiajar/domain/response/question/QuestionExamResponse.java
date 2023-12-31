package xcode.marsiajar.domain.response.question;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.response.answer.AnswerResponse;

import java.util.List;

@Getter
@Setter
public class QuestionExamResponse {
    private String secureId;
    private String content;
    private List<AnswerResponse> answers;
    private String discussion;
}
