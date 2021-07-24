package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamKeyResponse {
    private String question;
    private String answer;
    private String discussion;
}
