package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;

import java.util.Date;

@Getter
@Setter
public class ExamResultResponse {
    private QuestionSubTypeEnum questionSubType;
    private Date date;
    private int score;
    private int correct;
    private int total;
}
