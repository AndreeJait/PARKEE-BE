package com.andree.panjaitan.parkeebe.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypeRequest {
    @JsonProperty("vehicle_name")
    @NotEmpty(message = "vehicle name is required")
    @Length(min = 3, message = "minimum 3 characters")
    private String vehicleName;
}
