package xcode.ilmugiziku.domain.request.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateScheduleRequest {
    private String authSecureId;
    private String desc;
    private Date startDate;
    private Date endDate;
    private int timeLimit;

    public CreateScheduleRequest() {
    }

    public boolean validate() {
        return timeLimit > 0
                && startDate.after(new Date())
                && endDate.after(new Date())
                && !authSecureId.isEmpty();
    }
}
