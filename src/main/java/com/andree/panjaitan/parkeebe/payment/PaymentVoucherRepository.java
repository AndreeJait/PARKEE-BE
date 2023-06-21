package com.andree.panjaitan.parkeebe.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, UUID> {
}
