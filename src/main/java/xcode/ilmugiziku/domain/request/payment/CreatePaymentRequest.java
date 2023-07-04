package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.PackageTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull()
    private PackageTypeEnum packageType;
    @NotBlank()
    private String successRedirectUrl;
    @NotBlank()
    private String failureRedirectUrl;

    public CreatePaymentRequest() {
    }

}
