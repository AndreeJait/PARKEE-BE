package com.andree.panjaitan.parkeebe.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedLocation {
    @JsonProperty("vehicle_type")
    String vehicleType;

    @JsonProperty("vehicle_type_id")
    UUID vehicleTypeId;


    @JsonProperty("booked_count")
    Integer bookedCount;

    Integer capacity;

    @JsonProperty("available_capacity")
    Integer availableCapacity;
}
