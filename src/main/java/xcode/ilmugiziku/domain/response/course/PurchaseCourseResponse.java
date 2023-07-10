package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseCourseResponse {
    private String invoiceId;
    private String invoiceUrl;
    private Date invoiceDeadline;
}
