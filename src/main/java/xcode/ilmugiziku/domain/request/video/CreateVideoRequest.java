package xcode.ilmugiziku.domain.request.video;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVideoRequest {
    private String uri;
    private int questionType;
    private int questionSubType;

    public CreateVideoRequest() {
    }

    public boolean validate() {
        return !uri.isEmpty();
    }
}
