package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.model.AuthModel;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.stringToArray;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;

@Getter
@Setter
public class CreatePaymentRequest {
    private String packageSecureId;
    private String invoiceId;
    private int packageType;
    private Date paymentDeadline;

    public CreatePaymentRequest() {
    }

    public boolean isUpgradePackage(AuthModel authModel) {
        boolean result = false;

        if (authModel.isPremium()) {
            if (packageType == UKOM_EXPERT) {
                for (String type : stringToArray(authModel.getPackages())) {
                    if (Integer.parseInt(type) == UKOM_NEWBIE) {
                        result = true;
                    }
                }
            }

            if (packageType == SKB_EXPERT) {
                for (String type : stringToArray(authModel.getPackages())) {
                    if (Integer.parseInt(type) == SKB_NEWBIE) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }
}
