package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaboratoryValueResponse {
    private String secureId;
    private String content;
    private String value;
}
