package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamVideoResponse {
    private String uri;
    private int questionSubType;

    public ExamVideoResponse(String uri, int questionSubType) {
        this.uri = uri;
        this.questionSubType = questionSubType;
    }

    public ExamVideoResponse() {}
}
