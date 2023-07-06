package xcode.ilmugiziku.domain.request.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseBenefitRequest {
    private String secureId;
    private boolean isAvailable;

    public CourseBenefitRequest() {
    }
}
