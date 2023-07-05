package xcode.ilmugiziku.domain.request.benefit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateBenefitRequest {
    private String desc;

    public CreateUpdateBenefitRequest() {
    }
}
