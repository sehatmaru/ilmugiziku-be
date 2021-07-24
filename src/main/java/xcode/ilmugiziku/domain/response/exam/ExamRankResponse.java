package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamRankResponse {
    private String fullName;
    private String email;
    private int correct;
    private int total;
}
