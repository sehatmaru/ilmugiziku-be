package xcode.ilmugiziku.domain.request.laboratory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLaboratoryValueRequest {
    private String secureId;
    private String measure;
    private String unit;
    private String referenceValue;

    public UpdateLaboratoryValueRequest() {
    }

}
