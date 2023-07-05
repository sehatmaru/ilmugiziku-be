package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateExamRequest {
    private ExamRequest[] exams;
    @NotNull()
    private QuestionTypeEnum questionType;
    @NotNull()
    private QuestionSubTypeEnum questionSubType;

    public CreateExamRequest() {
    }

}
