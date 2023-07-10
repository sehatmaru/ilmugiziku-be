package xcode.ilmugiziku.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceResponse {
    private String packageName;
    private int totalAmount;
}
