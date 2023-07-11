package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateUpdateTemplateRequest {
    @NotEmpty()
    private String name;

    public CreateUpdateTemplateRequest() {
    }

}
