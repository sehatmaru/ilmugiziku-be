package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;

@Getter
@Setter
public class ExamInformationResponse {
    private QuestionSubTypeEnum questionSubType;
    private int questionTotal;
    private int timeLimit;
    private boolean isOpen;

    public ExamInformationResponse(QuestionSubTypeEnum questionSubType, int questionTotal, int timeLimit, boolean isOpen) {
            this.questionSubType = questionSubType;
            this.questionTotal = questionTotal;
            this.timeLimit = timeLimit;
            this.isOpen = isOpen;
        }

}
