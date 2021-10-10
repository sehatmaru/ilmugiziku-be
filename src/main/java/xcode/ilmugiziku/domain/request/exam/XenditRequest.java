package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XenditRequest {
    private String id;
    private String external_id;
    private String user_id;
    private boolean is_high;
    private String payment_method;
    private String status;
    private String merchant_name;
    private int amount;
    private int paid_amount;
    private String bank_code;
    private String paid_at;
    private String payer_email;
    private String description;
    private int adjusted_received_amount;
    private String fees_paid_amount;
    private String updated;
    private String created;
    private String currency;
    private String payment_channel;
    private String payment_destination;

    public XenditRequest() {}

    @Override
    public String toString() {
        return "XenditRequest{" +
                "id='" + id + '\'' +
                ", external_id='" + external_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", is_high=" + is_high +
                ", payment_method='" + payment_method + '\'' +
                ", status='" + status + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", amount=" + amount +
                ", paid_amount=" + paid_amount +
                ", bank_code='" + bank_code + '\'' +
                ", paid_at='" + paid_at + '\'' +
                ", payer_email='" + payer_email + '\'' +
                ", description='" + description + '\'' +
                ", adjusted_received_amount=" + adjusted_received_amount +
                ", fees_paid_amount='" + fees_paid_amount + '\'' +
                ", updated='" + updated + '\'' +
                ", created='" + created + '\'' +
                ", currency='" + currency + '\'' +
                ", payment_channel='" + payment_channel + '\'' +
                ", payment_destination='" + payment_destination + '\'' +
                '}';
    }
}
