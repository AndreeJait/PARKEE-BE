package com.andree.panjaitan.parkeebe.voucher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    Boolean existsByCodeVoucherAndEffectiveDateLessThanEqualAndExpiredAtGreaterThanEqual(String codeVoucher, LocalDateTime localDateTime, LocalDateTime localDateTime2);
    Optional<Voucher> findByCodeVoucher(String codeVoucher);
}
