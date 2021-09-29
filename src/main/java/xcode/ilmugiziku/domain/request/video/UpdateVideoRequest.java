package xcode.ilmugiziku.domain.request.video;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.UKOM;

@Getter
@Setter
public class UpdateVideoRequest {
    private String secureId;
    private String competence;
    private String uri;
    private int theoryType;

    public UpdateVideoRequest() {
    }

    public boolean validate() {
        return theoryType == UKOM || theoryType == SKB_GIZI;
    }
}
