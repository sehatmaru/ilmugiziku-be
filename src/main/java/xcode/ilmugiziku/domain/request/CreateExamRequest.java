package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.PRACTICE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

@Getter
@Setter
public class CreateExamRequest {
    private String[] questionsSecureId;
    private String[] answersSecureId;
    private String authSecureId;
    private String questionType;
    private String questionSubType;
    private List<CreateAnswerRequest> answers;

    public CreateExamRequest() {
    }

    public boolean validate() {
        boolean result = true;

//        if (answers.size() != 5) {
//            result = false;
//        } else {
//            int count = 0;
//
//            for (CreateAnswerRequest answer : answers) {
//                if (answer.isValue()) {
//                    count+=1;
//                }
//            }
//
//            if (count > 1) {
//                result = false;
//            }
//        }
//
//        if (questionType != QUIZ && questionType != PRACTICE) {
//            result = false;
//        }

        return result;
    }
}
