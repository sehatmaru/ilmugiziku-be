package xcode.ilmugiziku.domain.request.webinar;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateWebinarRequest {
    private String title;
    private String link;
    private Date date;
    private String meetingId;
    private String passcode;

    public UpdateWebinarRequest() {
    }

    public boolean validate() {
        return !title.isEmpty();
    }
}
