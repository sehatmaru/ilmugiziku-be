package xcode.ilmugiziku.domain.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreatePaymentResponse {
    private String invoiceId;
    private String invoiceUrl;
    private String paymentDeadline;
}
