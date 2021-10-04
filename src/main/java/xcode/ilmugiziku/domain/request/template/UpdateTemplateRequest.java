package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTemplateRequest {
    private String secureId;
    private String name;
    private int questionType;
    private int questionSubType;

    public UpdateTemplateRequest() {
    }

    public boolean validate() {
        return !name.isEmpty();
    }
}
