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

    public ScheduleDateRequest() {
    }

    public boolean validate() {
        return startDate != null && endDate != null;
    }
}
