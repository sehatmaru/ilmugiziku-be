package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.TheoryTypeEnum;

@Getter
@Setter
public class TheoryResponse {
    private String secureId;
    private String competence;
    private String uri;
    private TheoryTypeEnum theoryType;
}
