package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateScheduleRequest {
    private String authSecureId;
    private String desc;
    private Date schedule;

    public CreateScheduleRequest() {
    }
}
