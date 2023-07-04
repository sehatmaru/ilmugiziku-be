package xcode.ilmugiziku.domain.request.discussionvideo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateDiscussionVideoRequest {
    @NotBlank()
    private String uri;
    private String templateSecureId;
    private int questionType;
    private int questionSubType;

    public CreateDiscussionVideoRequest() {
    }

}
