package xcode.marsiajar.domain.response.benefit;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BenefitResponse {
    private String secureId;
    private String desc;
    private Date createdAt;
}
