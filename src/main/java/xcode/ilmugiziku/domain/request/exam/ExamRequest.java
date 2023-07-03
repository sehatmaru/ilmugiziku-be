package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamRequest {
    private String questionsSecureId;
    private String answersSecureId;

    public ExamRequest() {
    }

    public boolean validate() {
        return !questionsSecureId.isEmpty();
    }
}
