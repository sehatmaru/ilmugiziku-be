package xcode.ilmugiziku.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;
import xcode.ilmugiziku.domain.enums.LearningTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class InvoiceDetailResponse {
    private String secureId;
    private String relSecureId;
    private String consumerName;
    private String invoiceId;
    private String invoiceUrl;
    private Date paidDate;
    private InvoiceStatusEnum invoiceStatus;
    private LearningTypeEnum invoiceType;
    private Date invoiceDeadline;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentChannel;
    private String bankCode;
    private Date createdAt;
    private Date deletedAt;
}
