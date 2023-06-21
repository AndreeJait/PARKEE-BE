package com.andree.panjaitan.parkeebe.voucher;

import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voucher")
@Tag(name = "Voucher")
public class VoucherController {
    private final VoucherService service;

    @GetMapping("/check/{code_voucher}")
    SuccessResponse<Boolean> checkVoucher(@PathVariable("code_voucher") String codeVoucher){
        return new SuccessResponse<>(service.checkVoucher(codeVoucher));
    }

    @GetMapping("")
    SuccessResponse<List<Voucher>> getAllVoucher(){
        return new SuccessResponse<>(service.getAll());
    }
}
