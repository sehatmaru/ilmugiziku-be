package xcode.ilmugiziku.domain.request.discussionvideo;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;

@Getter
@Setter
public class UpdateDiscussionVideoRequest {
    private String secureId;
    private String uri;
    private QuestionTypeEnum questionType;
    private QuestionSubTypeEnum questionSubType;

    public UpdateDiscussionVideoRequest() {
    }

    public boolean validate() {
        return !uri.isEmpty() && !secureId.isEmpty();
    }
}
