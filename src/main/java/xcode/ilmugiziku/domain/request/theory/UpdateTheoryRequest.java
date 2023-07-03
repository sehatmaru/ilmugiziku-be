package xcode.ilmugiziku.domain.request.theory;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.TheoryTypeRefs.UKOM;

@Getter
@Setter
public class UpdateTheoryRequest {
    private String secureId;
    private String competence;
    private String uri;
    private int theoryType;

    public UpdateTheoryRequest() {
    }

    public boolean validate() {
        return theoryType == UKOM || theoryType == SKB_GIZI;
    }
}
