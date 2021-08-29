package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamStatusResponse {
    private boolean isOpen;
    private int questionSubType;

    public ExamStatusResponse(boolean isOpen, int questionSubType) {
        this.isOpen = isOpen;
        this.questionSubType = questionSubType;
    }
}
