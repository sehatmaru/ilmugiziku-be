package xcode.ilmugiziku.domain.request.theory;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;

@Getter
@Setter
public class UpdateTheoryRequest {
    private String secureId;
    private String competence;
    private String uri;
    private TheoryTypeEnum theoryType;

    public UpdateTheoryRequest() {
    }

}
