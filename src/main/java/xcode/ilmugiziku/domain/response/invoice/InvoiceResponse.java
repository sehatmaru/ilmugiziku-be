package xcode.ilmugiziku.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceResponse {
    private String packageName;
    private BigDecimal totalAmount;
}