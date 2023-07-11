package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExamRequest {
    private ExamRequest[] exams;
//    @NotNull()
//    private QuestionTypeEnum questionType;
//    @NotNull()
//    private QuestionSubTypeEnum questionSubType;

    public CreateExamRequest() {
    }

}
