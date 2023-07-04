package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;

@Getter
@Setter
public class ExamVideoResponse {
    private String uri;
    private QuestionSubTypeEnum questionSubType;

    public ExamVideoResponse(String uri, QuestionSubTypeEnum questionSubType) {
        this.uri = uri;
        this.questionSubType = questionSubType;
    }

    public ExamVideoResponse() {}
}
