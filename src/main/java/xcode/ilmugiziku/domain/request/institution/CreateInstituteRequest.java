package xcode.ilmugiziku.domain.request.institution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInstituteRequest {
    private String uri;
    private String description;

    public CreateInstituteRequest() {
    }

    public boolean validate() {
        return !uri.isEmpty();
    }
}
