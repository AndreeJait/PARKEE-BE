package com.andree.panjaitan.parkeebe.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCheckInOutLocation {
    @JsonProperty("location_id")
    String locationId;
}
