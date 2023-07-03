package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamRankResponse {
    private String fullName;
    private int correct;
    private int total;
}
