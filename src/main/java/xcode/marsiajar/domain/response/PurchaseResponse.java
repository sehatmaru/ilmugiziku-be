package xcode.marsiajar.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseResponse {
    private String invoiceId;
    private String invoiceUrl;
    private Date invoiceDeadline;
}
