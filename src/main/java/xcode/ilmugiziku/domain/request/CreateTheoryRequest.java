package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.UKOM;

@Getter
@Setter
public class CreateTheoryRequest {
    private String competence;
    private String uri;
    private int theoryType;

    public CreateTheoryRequest() {
    }

    public boolean validate() {
        return theoryType == UKOM || theoryType == SKB_GIZI;
    }
}
