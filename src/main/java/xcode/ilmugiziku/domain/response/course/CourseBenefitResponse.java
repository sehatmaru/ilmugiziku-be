package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseBenefitResponse {
    private String secureId;
    private String desc;
    private boolean isAvailable;

    public CourseBenefitResponse(String secureId, String desc, boolean isAvailable) {
        this.secureId = secureId;
        this.desc = desc;
        this.isAvailable = isAvailable;
    }
}
