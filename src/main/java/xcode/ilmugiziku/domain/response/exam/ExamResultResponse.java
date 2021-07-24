package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamResultResponse {
    private int questionSubType;
    private Date date;
    private int score;
    private int correct;
    private int total;
}
