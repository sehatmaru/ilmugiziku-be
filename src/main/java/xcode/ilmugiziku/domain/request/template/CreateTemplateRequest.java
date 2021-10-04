package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTemplateRequest {
    private String name;
    private int questionType;
    private int questionSubType;

    public CreateTemplateRequest() {
    }

    public boolean validate() {
        return !name.isEmpty();
    }
}
