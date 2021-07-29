package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleResponse {
    private String secureId;
    private String desc;
    private Date startDate;
    private Date endDate;
    private int timeLimit;
}
