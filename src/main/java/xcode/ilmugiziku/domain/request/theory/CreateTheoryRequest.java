package xcode.ilmugiziku.domain.request.theory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateTheoryRequest {
    private String competence;
    private String uri;
    @NotNull()
    private int theoryType;

    public CreateTheoryRequest() {
    }

}
