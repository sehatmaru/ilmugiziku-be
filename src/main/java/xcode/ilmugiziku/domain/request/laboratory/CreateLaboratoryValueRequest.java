package xcode.ilmugiziku.domain.request.laboratory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLaboratoryValueRequest {
    private String content;
    private String value;

    public CreateLaboratoryValueRequest() {
    }
}
