package xcode.ilmugiziku.domain.request.discussionvideo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDiscussionVideoRequest {
    private String secureId;
    private String uri;
    private int questionType;
    private int questionSubType;

    public UpdateDiscussionVideoRequest() {
    }

    public boolean validate() {
        return !uri.isEmpty() && !secureId.isEmpty();
    }
}
