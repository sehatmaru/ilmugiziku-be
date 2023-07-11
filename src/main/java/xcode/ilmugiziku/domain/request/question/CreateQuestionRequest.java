package xcode.ilmugiziku.domain.request.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CreateQuestionRequest {
    private String templateSecureId;
    @NotBlank()
    private String content;
    @NotBlank()
    private String discussion;
    private String label;
    private String type;
    private List<CreateAnswerRequest> answers;

    public CreateQuestionRequest() {
    }

    // TODO: 11/07/23
//    public boolean isValid() {
//        boolean result = true;
//
//        if (answers.size() != 5) result = false;
//        else {
//            int count = 0;
//
//            for (CreateAnswerRequest answer : answers) {
//                if (answer.isValue()) count+=1;
//            }
//
//            if (questionType == QUIZ || questionType == PRACTICE && (questionSubType != NONE)) {
//                result = false;
//            }
//
//            if (count > 1) result = false;
//        }
//
//        return result;
//    }
}
