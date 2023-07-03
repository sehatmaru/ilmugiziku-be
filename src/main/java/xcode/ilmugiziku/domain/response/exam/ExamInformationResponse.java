package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamInformationResponse {
    private int questionSubType;
    private int questionTotal;
    private int timeLimit;
    private boolean isOpen;

    public ExamInformationResponse(int questionSubType, int questionTotal, int timeLimit, boolean isOpen) {
            this.questionSubType = questionSubType;
            this.questionTotal = questionTotal;
            this.timeLimit = timeLimit;
            this.isOpen = isOpen;
        }

}
