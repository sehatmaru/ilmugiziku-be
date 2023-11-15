package xcode.marsiajar.domain.response.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionListResponse {
    private String secureId;
    private String content;
    private String category;
    private String categorySecureId;
    private String createdBy;
    private String editedBy;

}
