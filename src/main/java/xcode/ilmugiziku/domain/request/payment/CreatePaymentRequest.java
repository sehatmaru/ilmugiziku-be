package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull()
    private int packageType;
    @NotBlank()
    private String successRedirectUrl;
    @NotBlank()
    private String failureRedirectUrl;

    public CreatePaymentRequest() {
    }

}
