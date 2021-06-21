package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.PRACTICE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.QUIZ;

@Getter
@Setter
public class CreateScheduleRequest {
    private String authSecureId;
    private List<Date> dates;

    public CreateScheduleRequest() {
    }

    public boolean validate() {
        return dates.size() >= 1;
    }
}
