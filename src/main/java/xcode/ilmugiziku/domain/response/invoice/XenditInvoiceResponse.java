package xcode.ilmugiziku.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;

import java.util.Date;

@Getter
@Setter
public class XenditInvoiceResponse {
    private String invoiceId;
    private InvoiceStatusEnum status;
    private Date paidDate;
}
