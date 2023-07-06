package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseBenefitResponse {
    private String secureId;
    private String desc;

    public CourseBenefitResponse(String secureId, String desc) {
        this.secureId = secureId;
        this.desc = desc;
    }
}
