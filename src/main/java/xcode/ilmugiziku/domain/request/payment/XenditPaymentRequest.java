package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;

import static xcode.ilmugiziku.domain.enums.PaymentStatusEnum.EXPIRED;
import static xcode.ilmugiziku.domain.enums.PaymentStatusEnum.PAID;
import com.google.gson.annotations.SerializedName;

@Getter
@Setter
public class XenditPaymentRequest {
    private String id;
    @SerializedName("external_id")
    private String externalId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("is_high")
    private boolean isHigh;
    @SerializedName("payment_method")
    private String paymentMethod;
    private PaymentStatusEnum status;
    @SerializedName("merchant_name")
    private String merchantName;
    private int amount;
    @SerializedName("paid_amount")
    private int paidAmount;
    @SerializedName("bank_code")
    private String bankCode;
    @SerializedName("paid_at")
    private String paidAt;
    @SerializedName("payer_email")
    private String payerEmail;
    private String description;
    @SerializedName("adjusted_received_amount")
    private int adjustedReceivedAmount;
    @SerializedName("fees_paid_amount")
    private String feesPaidAmount;
    private String updated;
    private String created;
    private String currency;
    @SerializedName("payment_channel")
    private String paymentChannel;
    @SerializedName("payment_destination")
    private String paymentDestination;

    public XenditPaymentRequest() {
    }

    public boolean isPaid() {
        return status.equals(PAID);
    }

    public boolean isExpired() {
        return status.equals(EXPIRED);
    }

}
