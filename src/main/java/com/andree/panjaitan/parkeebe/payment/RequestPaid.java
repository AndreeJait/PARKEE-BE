package com.andree.panjaitan.parkeebe.payment;

import com.andree.panjaitan.parkeebe.voucher.Voucher;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPaid {
    @JsonProperty("code_payment")
    private String codePayment;

    @JsonProperty("payment_method")
    private String paymentMethod;

    List<String> vouchers;
}
