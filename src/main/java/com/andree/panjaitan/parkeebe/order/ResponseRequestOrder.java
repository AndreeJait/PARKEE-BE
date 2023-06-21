package com.andree.panjaitan.parkeebe.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRequestOrder {
    @JsonProperty("link_parking_ticket")
    String linkParkingTicket;
}
