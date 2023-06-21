package com.andree.panjaitan.parkeebe.payment;

import com.andree.panjaitan.parkeebe.order.Order;
import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.andree.panjaitan.parkeebe.voucher.Voucher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @JsonProperty("code_paid")
    private String codePaid;

    @OneToOne
    @JoinColumn(columnDefinition = "order_id")
    @JsonIgnoreProperties(value = {"location", "guard", "vehicle"})
    @JsonIgnore
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @JsonProperty("total_price")
    private BigInteger totalPrice;
    @JsonProperty("original_price")
    private BigInteger originalPrice;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties(value = {"payment"})
    private List<PaymentVoucher> vouchers;

    @JsonProperty("paid_at")
    LocalDateTime paidAt;
}
