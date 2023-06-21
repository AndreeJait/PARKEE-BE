package com.andree.panjaitan.parkeebe.voucher;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Voucher extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @JsonProperty("code_voucher")
    private String codeVoucher;

    @JsonProperty("is_percentage")
    private boolean isPercentage;

    private BigInteger value;

    @JsonProperty("effective_date")
    private LocalDateTime effectiveDate;

    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;
}
