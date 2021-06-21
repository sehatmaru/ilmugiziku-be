package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLaboratoryValueRequest {
    private String secureId;
    private String content;
    private String value;

    public UpdateLaboratoryValueRequest() {
    }

}
