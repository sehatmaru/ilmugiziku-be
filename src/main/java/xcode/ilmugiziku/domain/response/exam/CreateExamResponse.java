package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExamResponse {
    private String secureId;
    private int answered;
    private int blank;
}
