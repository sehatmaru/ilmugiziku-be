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
    private Date date;
    private String startTime;
    private String endTime;

    public ScheduleDateRequest() {
    }

    public boolean validate() {
        return date != null && startTime != null && endTime != null;
    }
}
