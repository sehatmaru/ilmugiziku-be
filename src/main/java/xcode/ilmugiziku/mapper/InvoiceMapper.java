package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.enums.InvoiceTypeEnum;
import xcode.ilmugiziku.domain.model.CourseModel;
import xcode.ilmugiziku.domain.model.InvoiceModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.model.WebinarModel;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.response.PurchaseResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static xcode.ilmugiziku.domain.enums.InvoiceStatusEnum.PENDING;
import static xcode.ilmugiziku.domain.enums.InvoiceTypeEnum.COURSE;

public class InvoiceMapper {

    public InvoiceModel createRequestToModel(PurchaseRequest request, PurchaseResponse invoice) {
        if (request != null) {
            InvoiceModel response = new InvoiceModel();
            response.setInvoiceStatus(PENDING);
            response.setCreatedAt(new Date());
            response.setInvoiceId(invoice.getInvoiceId());
            response.setInvoiceUrl(invoice.getInvoiceUrl());
            response.setInvoiceDeadline(invoice.getInvoiceDeadline());

            return response;
        } else {
            return null;
        }
    }

    public Map<String, Object> createInvoiceRequest(
            UserModel user,
            String fullName,
            PurchaseRequest request,
            InvoiceTypeEnum type,
            WebinarModel webinarModel,
            CourseModel courseModel,
            String secureId
    ) {
        String title = type == COURSE ? courseModel.getTitle() : webinarModel.getTitle();
        BigDecimal price = type == COURSE ? courseModel.getPrice() : webinarModel.getPrice();

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
        item.put("name", title);
        item.put("quantity", 1);
        item.put("price", price);
        Map[] items = new Map[]{item};

        Map<String, Object> params = new HashMap<>();
        params.put("external_id", secureId);
        params.put("amount", price);
        params.put("currency", "IDR");
        params.put("payer_email", user.getEmail());
        params.put("customer", customer);
        params.put("customer_notification_preference", notification);
        params.put("items", items);
        params.put("should_send_email", true);
        params.put("description", "Paket " + title + " selama 6 bulan");
        params.put("success_redirect_url", request.getSuccessRedirectUrl());
        params.put("failure_redirect_url", request.getFailureRedirectUrl());

        params.forEach((key, value) -> System.out.println(key + " : " + value));

        return params;
    }
}
