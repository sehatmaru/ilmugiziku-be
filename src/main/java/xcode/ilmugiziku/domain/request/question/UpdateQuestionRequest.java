package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.NONE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

@Getter
@Setter
public class UpdateQuestionRequest {
    private String secureId;
    private String content;
    private int questionType;
    private int questionSubType;
    private String discussion;
    private String label;
    private List<UpdateAnswerRequest> answers;

    public UpdateQuestionRequest() {
    }

    public boolean validate() {
        boolean result = true;

        if (answers.size() != 5) {
            result = false;
        } else {
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
        }

        if (questionType  < 1 || questionType > 4) {
            result = false;
        }

        if (questionType == QUIZ || questionType == PRACTICE) {
            if (questionSubType != NONE) {
                result = false;
            }
        } else if (questionType == TRY_OUT_SKB_GIZI || questionType == TRY_OUT_UKOM) {
            if (questionSubType < 1 || questionSubType > 4) {
                result = false;
            }
        }

        if (secureId == null) {
            result = false;
        }

        if (content.isEmpty() || discussion.isEmpty()) {
            result = false;
        }

        return result;
    }
}
