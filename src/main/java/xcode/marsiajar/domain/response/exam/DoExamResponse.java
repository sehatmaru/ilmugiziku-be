package xcode.marsiajar.domain.response.exam;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.response.question.QuestionResponse;

import java.util.List;

@Getter
@Setter
public class DoExamResponse {
    private String title;
    private int duration;
    private List<QuestionResponse> questions;
}
