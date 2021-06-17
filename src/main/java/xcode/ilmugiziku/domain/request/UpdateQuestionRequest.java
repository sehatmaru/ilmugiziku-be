package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateQuestionRequest {
    private String content;
    private int questionType;
    private List<UpdateAnswerRequest> answers;

    public UpdateQuestionRequest() {
    }
}
