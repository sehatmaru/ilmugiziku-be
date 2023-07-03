package xcode.ilmugiziku.domain.request.webinar;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;


@Getter
@Setter
public class CreateWebinarRequest {
    private String title;
    private String link;
    private Date date;
    private String meetingId;
    private String passcode;
    private int bimbelType;

    public CreateWebinarRequest() {
    }

    public boolean validate() {
        return !title.isEmpty() && (bimbelType == UKOM || bimbelType == SKB_GIZI) ;
    }
}
