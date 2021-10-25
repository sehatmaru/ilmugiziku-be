package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.request.payment.CreatePaymentRequest;

import java.util.Date;

import static xcode.ilmugiziku.shared.refs.PaymentStatusRefs.PENDING;

public class PaymentMapper {

    public PaymentModel createRequestToModel(CreatePaymentRequest request) {
        if (request != null) {
            Date expire = new Date();
            expire.setMonth(expire.getMonth() + 6);

            PaymentModel response = new PaymentModel();
            response.setPackageType(request.getPackageType());
            response.setPaymentStatus(PENDING);
            response.setExpiredDate(expire);
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }
}
