package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionListResponse {
    private String secureId;
    private String content;
    private String createdBy;
    private String editedBy;

}
