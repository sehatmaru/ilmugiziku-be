package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamVideoResponse {
    private String uri;

    // TODO: 11/07/23
//    public ExamVideoResponse(String uri, QuestionSubTypeEnum questionSubType) {
//        this.uri = uri;
//        this.questionSubType = questionSubType;
//    }

    public ExamVideoResponse() {}
}
