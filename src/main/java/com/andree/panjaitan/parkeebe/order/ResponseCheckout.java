package com.andree.panjaitan.parkeebe.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCheckout {
    @JsonProperty("code_payment")
    String codePayment;

    @JsonProperty("total_price")
    BigInteger totalPrice;

    @JsonProperty("total_dif")
    String totalDif;

    @JsonProperty("entry_time")
    String entryTime;

    @JsonProperty("exit_time")
    String exitTime;

    @JsonProperty("vehicle_type")
    String vehicleType;

}
