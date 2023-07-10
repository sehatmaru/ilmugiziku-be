package xcode.ilmugiziku.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateInvoiceResponse {
    private String invoiceId;
    private String invoiceUrl;
    private Date invoiceDeadline;
}
