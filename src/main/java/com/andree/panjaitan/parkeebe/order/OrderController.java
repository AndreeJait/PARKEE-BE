package com.andree.panjaitan.parkeebe.order;

import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import com.andree.panjaitan.parkeebe.utils.PrincipalUtils;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderController {

    private final OrderService service;
    private final PrincipalUtils principalUtils;

    @PostMapping("/request")
    public SuccessResponse<ResponseRequestOrder> requestOrder(
            @Valid @RequestBody RequestOrder request, Principal principal) throws DocumentException, IOException, WriterException {
        return new SuccessResponse<>(
                service.requestOrder(request, principalUtils.getUser(principal)));
    }

    @PostMapping("/checkout/barcode")
    public SuccessResponse<ResponseCheckout> checkoutBarcode(@Valid @RequestBody RequestCheckout request, Principal principal) {
        return new SuccessResponse<>(service.checkoutBarcode(request, principalUtils.getUser(principal)));
    }

    @PostMapping("/checkout/manual")
    public SuccessResponse<ResponseCheckout> checkoutManual(@Valid @RequestBody RequestCheckout request, Principal principal) {
        return new SuccessResponse<>(service.checkoutManual(request, principalUtils.getUser(principal)));
    }
}
