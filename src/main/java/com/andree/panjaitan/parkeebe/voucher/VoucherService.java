package com.andree.panjaitan.parkeebe.voucher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository repository;


    public Boolean checkVoucher(String codeVoucher) {
        LocalDateTime timeNow = LocalDateTime.now();
        return repository
                .existsByCodeVoucherAndEffectiveDateLessThanEqualAndExpiredAtGreaterThanEqual(
                        codeVoucher, timeNow, timeNow);
    }

    public List<Voucher> getAll() {
        return repository.findAll();
    }
}
