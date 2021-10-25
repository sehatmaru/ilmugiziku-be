package xcode.ilmugiziku.domain.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class XenditPaymentResponse {
    private String invoiceId;
    private String status;
    private Date paidDate;
}
