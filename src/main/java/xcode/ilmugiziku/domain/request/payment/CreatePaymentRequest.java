package xcode.ilmugiziku.domain.request.payment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotBlank()
    private String successRedirectUrl;
    @NotBlank()
    private String failureRedirectUrl;

    public CreatePaymentRequest() {
    }

}
