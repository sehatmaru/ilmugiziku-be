package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateScheduleRequest {
    private String authSecureId;
    private List<ScheduleDateRequest> schedules;

    public UpdateScheduleRequest() {
    }

    public boolean validate() {
        boolean result = true;

        for (ScheduleDateRequest schedule: schedules) {
            if (!schedule.validate()) {
                result = false;
            }
        }

        return result;
    }
}
