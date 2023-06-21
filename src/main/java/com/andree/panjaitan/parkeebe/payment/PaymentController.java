package com.andree.panjaitan.parkeebe.payment;

import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import com.andree.panjaitan.parkeebe.utils.PrincipalUtils;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment")
public class PaymentController {
    private final PaymentService service;
    private final PrincipalUtils principalUtils;

    @GetMapping("/{payment_id}")
    public SuccessResponse<Payment> findPaymentByID(@PathVariable("payment_id") String paymentId){
        return new SuccessResponse<>(service.findPaymentByID(UUID.fromString(paymentId)));
    }

    @GetMapping("/code/{code_payment}")
    public SuccessResponse<Payment> findPaymentByCodePayment(@PathVariable("code_payment") String codePayment){
        return new SuccessResponse<>(service.findPaymentByCodePayment(codePayment));
    }

    @PostMapping("")
    public SuccessResponse<ResponsePaid> paid(@RequestBody RequestPaid request, Principal principal) throws DocumentException, FileNotFoundException {
        return new SuccessResponse<ResponsePaid>(service.paid(request, principalUtils.getUser(principal)));
    }
}
