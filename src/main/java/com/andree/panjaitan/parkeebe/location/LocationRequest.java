package com.andree.panjaitan.parkeebe.location;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequest {
    @Length(min = 1, message = "must have capacity")
    List<VehicleCapacity> capacities;

    @NotEmpty(message = "name is required")
    String name;

    @NotEmpty(message = "address is required")
    String address;

    @NotEmpty(message = "city is required")
    String city;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class VehicleCapacity {
    String vehicleID;
    Integer capacity;
}
