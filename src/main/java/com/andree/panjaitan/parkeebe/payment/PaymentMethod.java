package com.andree.panjaitan.parkeebe.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
    CASH("cash"),
    ;
    @Getter
    private final String name;
}
