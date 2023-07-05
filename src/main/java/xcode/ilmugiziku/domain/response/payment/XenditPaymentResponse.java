package xcode.ilmugiziku.domain.response.payment;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;

import java.util.Date;

@Getter
@Setter
public class XenditPaymentResponse {
    private String invoiceId;
    private PaymentStatusEnum status;
    private Date paidDate;
}
