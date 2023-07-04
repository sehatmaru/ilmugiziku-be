package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateExamRequest {
    private ExamRequest[] exams;
    @NotNull()
    private int questionType;
    @NotNull()
    private int questionSubType;

    public CreateExamRequest() {
    }

}
