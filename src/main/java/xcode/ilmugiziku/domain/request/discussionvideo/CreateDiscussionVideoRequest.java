package xcode.ilmugiziku.domain.request.discussionvideo;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateDiscussionVideoRequest {
    @NotBlank()
    private String uri;
    private String templateSecureId;
    private QuestionTypeEnum questionType;
    private QuestionSubTypeEnum questionSubType;

    public CreateDiscussionVideoRequest() {
    }

}
