package xcode.ilmugiziku.domain.request.institution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInstituteRequest {
    private String secureId;
    private String uri;
    private String description;

    public UpdateInstituteRequest() {
    }

    public boolean validate() {
        return !secureId.isEmpty() && !uri.isEmpty();
    }
}
