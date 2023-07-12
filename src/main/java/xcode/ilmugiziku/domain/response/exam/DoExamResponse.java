package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;

import java.util.List;

@Getter
@Setter
public class DoExamResponse {
    private String title;
    private int time;
    private List<QuestionResponse> questions;
}
