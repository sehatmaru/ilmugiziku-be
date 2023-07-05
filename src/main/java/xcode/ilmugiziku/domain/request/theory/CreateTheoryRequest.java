package xcode.ilmugiziku.domain.request.theory;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateTheoryRequest {
    private String competence;
    private String uri;
    private TheoryTypeEnum theoryType;

    public CreateTheoryRequest() {
    }

}
