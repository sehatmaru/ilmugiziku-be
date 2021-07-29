package xcode.ilmugiziku.domain.request.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ScheduleDateRequest {
    private String scheduleSecureId;
    private String desc;
    private Date startDate;
    private Date endDate;
    private int timeLimit;

    public ScheduleDateRequest() {
    }

    public boolean validate() {
        return timeLimit > 0
                && startDate.after(new Date())
                && endDate.after(new Date())
                && !scheduleSecureId.isEmpty();
    }
}
