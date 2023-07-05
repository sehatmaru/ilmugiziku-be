package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.QuestionTypeEnum.*;
import static xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum.NONE;

@Getter
@Setter
public class UpdateQuestionRequest {
    @NotBlank()
    private String secureId;
    @NotBlank()
    private String content;
    private QuestionTypeEnum questionType;
    private QuestionSubTypeEnum questionSubType;
    @NotBlank()
    private String discussion;
    private String label;
    private String type;
    private List<UpdateAnswerRequest> answers;

    public UpdateQuestionRequest() {
    }

    public boolean isValid() {
        boolean result;

        if (answers.size() != 5) {
            result = false;
        } else {
            result = checkAnswer();
        }

        if (questionType == QUIZ || questionType == PRACTICE && (questionSubType != NONE)) {
            result = false;
        }

        return result;
    }

    private boolean checkAnswer() {
        boolean result = true;
        int count = 0;

        for (UpdateAnswerRequest answer : answers) {
            if (answer.isValue()) {
                count+=1;
            }

            if (answer.getSecureId() == null) {
                result = false;
            }
        }

        if (count > 1) {
            result = false;
        }

        return result;
    }
}
