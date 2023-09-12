package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.response.answer.AnswerResponse;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private String secureId;
    private String content;
    private String category;
    private String categorySecureId;
    private String createdBy;
    private String editedBy;
    private List<AnswerResponse> answers;

}
