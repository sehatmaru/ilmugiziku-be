package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ScheduleDateRequest {
    private String scheduleSecureId;
    private boolean valid;
    private Date date;

    public ScheduleDateRequest() {
    }

    public boolean validate() {
        return date != null;
    }
}
