package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamInformationResponse {
    private int questionSubType;
    private int questionTotal;
    private int timeLimit;

    public ExamInformationResponse(int questionSubType, int questionTotal, int timeLimit) {
        this.questionSubType = questionSubType;
        this.questionTotal = questionTotal;
        this.timeLimit = timeLimit;
    }
}
