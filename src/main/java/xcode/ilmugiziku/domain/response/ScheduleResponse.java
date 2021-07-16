package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ScheduleResponse {
    private String secureId;
    private String desc;
    private Date schedule;
    private String startTime;
    private String endTime;
}
