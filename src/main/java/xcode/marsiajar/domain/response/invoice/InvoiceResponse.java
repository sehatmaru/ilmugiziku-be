package xcode.marsiajar.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceResponse {
    private String title;
    private BigDecimal totalAmount;
}
