package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CreateUpdateQuestionRequest {

    @NotBlank()
    private String content;

    private List<CreateUpdateAnswerRequest> answers;

    private CourseTypeEnum category;

    public CreateUpdateQuestionRequest() {}

    public boolean isValid() {
        return answers.size() == 5;
    }

    public boolean isOneCorrectAnswer() {
        int correctAnswer = 0;

        for (CreateUpdateAnswerRequest answer : answers) {
            if (answer.isCorrectAnswer()) correctAnswer += 1;
        }

        return correctAnswer == 1;
    }

}
