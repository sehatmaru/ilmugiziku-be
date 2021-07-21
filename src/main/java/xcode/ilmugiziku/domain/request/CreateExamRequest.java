package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExamRequest {
    private ExamRequest[] exams;
    private int questionType;
    private int questionSubType;

    public CreateExamRequest() {
    }

    public boolean validate() {
        boolean result = questionType >= 0 && questionType <= 4;

        if (questionSubType < 0 || questionSubType > 4) {
            result = false;
        }

        for (ExamRequest answer : exams) {
            if (!answer.validate()) {
                result = false;
            }
        }

        return result;
    }
}
