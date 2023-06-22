package com.andree.panjaitan.parkeebe.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrder {

    @NotEmpty(message = "plat number is required")
    @Length(min = 3, message = "plat number minimum is 3")
    @JsonProperty("plat_number")
    private String platNumber;

    @JsonProperty("front_image")
    @NotEmpty(message = "car picture is required")
    private String frontImage;

    @JsonProperty("driver_image")
    @NotEmpty(message = "driver picture is required")
    private String driverImage;

    @JsonProperty("location_id")
    @NotEmpty(message = "location id is required")
    private String locationID;

    @JsonProperty("vehicle_type_id")
    @NotEmpty(message = "vehicle type is required")
    private String vehicleTypeID;
}
