package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.NONE;

@Getter
@Setter
public class CreateQuestionRequest {
    private String content;
    private int questionType;
    private int questionSubType;
    private String discussion;
    private String label;
    private String type;
    private List<CreateAnswerRequest> answers;

    public CreateQuestionRequest() {
    }

    public boolean validate() {
        boolean result = true;

        if (answers.size() != 5) {
            result = false;
        } else {
            int count = 0;

            for (CreateAnswerRequest answer : answers) {
                if (answer.isValue()) {
                    count+=1;
                }
            }

            if (count > 1) {
                result = false;
            }
        }

        if (questionType < 0 || questionType > 4) {
            result = false;
        } else {
            if (questionType == QUIZ || questionType == PRACTICE) {
                if (questionSubType != NONE) {
                    result = false;
                }
            }
        }

        if (content.isEmpty() || discussion.isEmpty()) {
            result = false;
        }

        return result;
    }
}
