package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.PaymentStatusRefs.SUCCEEDED;

@Getter
@Setter
public class XenditPaymentRequest {
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

    public XenditPaymentRequest() {
    }

    public boolean isSuccessPayment() {
        return status.equals(SUCCEEDED);
    }
}
