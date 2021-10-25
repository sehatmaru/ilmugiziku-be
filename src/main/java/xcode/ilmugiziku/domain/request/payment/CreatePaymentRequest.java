package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.model.AuthModel;

import static xcode.ilmugiziku.shared.Utils.stringToArray;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;

@Getter
@Setter
public class CreatePaymentRequest {
    private int packageType;
    private String successRedirectUrl;
    private String failureRedirectUrl;

    public CreatePaymentRequest() {
    }

    public boolean validate() {
        return packageType > 0 && packageType < 5;
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
