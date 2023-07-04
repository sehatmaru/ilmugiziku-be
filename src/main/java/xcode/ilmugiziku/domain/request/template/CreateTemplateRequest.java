package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateTemplateRequest {
    @NotBlank()
    private String name;
    private int questionType;
    private int questionSubType;

    public CreateTemplateRequest() {
    }

}
