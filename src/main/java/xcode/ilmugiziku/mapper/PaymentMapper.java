package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.enums.PaymentStatusEnum;
import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;
import xcode.ilmugiziku.domain.response.payment.CreatePaymentResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static xcode.ilmugiziku.domain.enums.PaymentStatusEnum.PENDING;

public class PaymentMapper {

    public PaymentModel createRequestToModel(CreatePaymentRequest request, CreatePaymentResponse payment) {
        if (request != null) {
            Date expire = new Date();
            expire.setMonth(expire.getMonth() + 6);

            PaymentModel response = new PaymentModel();
            response.setPackageType(request.getPackageType());
            response.setPaymentStatus(PENDING);
            response.setExpiredDate(expire);
            response.setCreatedAt(new Date());
            response.setInvoiceId(payment.getInvoiceId());
            response.setInvoiceUrl(payment.getInvoiceUrl());
            response.setPaymentDeadline(payment.getPaymentDeadline());

            return response;
        } else {
            return null;
        }
    }

    public Map<String, Object> createInvoiceRequest(UserModel user, String fullName, CreatePaymentRequest request, PackageModel packageModel, int fee, String secureId) {
        Map<String, Object> customer = new HashMap<>();
        customer.put("given_names", fullName);
        customer.put("email", user.getEmail());

        String[] preferences = {"email"};
        Map<String, Object> notification = new HashMap<>();
        notification.put("invoice_created", preferences);
        notification.put("invoice_reminder", preferences);
        notification.put("invoice_paid", preferences);
        notification.put("invoice_expired", preferences);

        Map<String, Object> item = new HashMap<>();
        item.put("name", packageModel.getTitle());
        item.put("quantity", 6);
        item.put("price", packageModel.getPrice());
        Map[] items = new Map[]{item};

        Map<String, Object> params = new HashMap<>();
        params.put("external_id", secureId);
        params.put("amount", fee);
        params.put("currency", "IDR");
        params.put("payer_email", user.getEmail());
        params.put("customer", customer);
        params.put("customer_notification_preference", notification);
        params.put("items", items);
        params.put("should_send_email", true);
        params.put("description", packageModel.getTitle() + " selama 6 bulan");

        if (!request.getSuccessRedirectUrl().isEmpty()) {
            params.put("success_redirect_url", request.getSuccessRedirectUrl());
        }

        if (!request.getFailureRedirectUrl().isEmpty()) {
            params.put("failure_redirect_url", request.getFailureRedirectUrl());
        }

        return params;
    }
}
