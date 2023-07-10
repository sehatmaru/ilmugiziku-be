package xcode.ilmugiziku.domain.request.invoice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateInvoiceRequest {
    @NotBlank()
    private String successRedirectUrl;
    @NotBlank()
    private String failureRedirectUrl;

    public CreateInvoiceRequest() {
    }

}
