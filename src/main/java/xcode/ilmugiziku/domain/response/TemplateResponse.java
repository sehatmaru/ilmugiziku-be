package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateResponse {
    private String secureId;
    private String name;
    private boolean isUsed;
}
