package xcode.ilmugiziku.domain.request.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;

import static xcode.ilmugiziku.domain.enums.InvoiceStatusEnum.EXPIRED;
import static xcode.ilmugiziku.domain.enums.InvoiceStatusEnum.PAID;

@Getter
@Setter
public class XenditInvoiceRequest {
    private String id;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("payment_method")
    private String paymentMethod;
    private InvoiceStatusEnum status;
    private int amount;
    @JsonProperty("paid_amount")
    private int paidAmount;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("paid_at")
    private String paidAt;
    @JsonProperty("payer_email")
    private String payerEmail;
    private String description;
    private String updated;
    private String created;
    private String currency;
    @JsonProperty("payment_channel")
    private String paymentChannel;
    @JsonProperty("payment_destination")
    private String paymentDestination;

    public XenditInvoiceRequest() {
    }

    public boolean isPaid() {
        return status.equals(PAID);
    }

    public boolean isExpired() {
        return status.equals(EXPIRED);
    }

}
