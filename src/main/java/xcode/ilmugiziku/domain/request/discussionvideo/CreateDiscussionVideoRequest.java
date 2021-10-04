package xcode.ilmugiziku.domain.request.discussionvideo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDiscussionVideoRequest {
    private String uri;
    private int questionType;
    private int questionSubType;

    public CreateDiscussionVideoRequest() {
    }

    public boolean validate() {
        return !uri.isEmpty();
    }
}
