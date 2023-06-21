package com.andree.panjaitan.parkeebe.payment;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.andree.panjaitan.parkeebe.voucher.Voucher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PaymentVoucher extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "payment_id")
    @JsonBackReference
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "voucher_id")
    @JsonBackReference
    private Voucher voucher;
}
