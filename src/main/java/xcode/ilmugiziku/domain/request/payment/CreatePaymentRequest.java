package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreatePaymentRequest {
    private String packageSecureId;
    private String invoiceId;
    private int packageType;
    private Date paymentDeadline;

    public CreatePaymentRequest() {
    }
}
