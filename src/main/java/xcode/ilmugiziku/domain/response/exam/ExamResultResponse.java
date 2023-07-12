package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamResultResponse {
    private String title;
    private int duration;
    private double score;
    private int correct;
    private int incorrect;
    private int blank;
}
