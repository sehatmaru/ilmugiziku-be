package xcode.marsiajar.domain.response.invoice;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.enums.InvoiceStatusEnum;
import xcode.marsiajar.domain.enums.LearningTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

import static xcode.marsiajar.domain.enums.LearningTypeEnum.COURSE;

@Getter
@Setter
public class InvoiceListResponse {
    private String secureId;
    private String relSecureId;
    private String consumerName;
    private String invoiceId;
    private InvoiceStatusEnum invoiceStatus;
    private LearningTypeEnum invoiceType;
    private String paymentChannel;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private Date createdAt;

    public boolean isCourseInvoice() {
        return invoiceType == COURSE;
    }
}
