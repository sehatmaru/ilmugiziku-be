package xcode.ilmugiziku.domain.response.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String packageName;
    private int totalAmount;
}
