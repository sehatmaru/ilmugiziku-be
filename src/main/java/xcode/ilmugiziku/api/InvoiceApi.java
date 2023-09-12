package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.invoice.XenditInvoiceRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.invoice.InvoiceResponse;
import xcode.ilmugiziku.domain.response.invoice.XenditInvoiceResponse;
import xcode.ilmugiziku.service.InvoiceService;

@RestController
@RequestMapping(value = "invoice")
public class InvoiceApi {

    @Autowired private InvoiceService invoiceService;

    @GetMapping("/detail")
    ResponseEntity<BaseResponse<InvoiceResponse>> detail(
            @RequestParam @Validated String categorySecureId
    ) {
        BaseResponse<InvoiceResponse> response = invoiceService.detailInvoice(categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/xendit/callback")
    ResponseEntity<BaseResponse<XenditInvoiceResponse>> xenditCallback(
            @RequestBody @Validated XenditInvoiceRequest request
    ) {
        BaseResponse<XenditInvoiceResponse> response = invoiceService.xenditCallback(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
