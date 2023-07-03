package xcode.ilmugiziku.domain.request.laboratory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLaboratoryValueRequest {
    private String measure;
    private String unit;
    private String referenceValue;

    public CreateLaboratoryValueRequest() {
    }
}
