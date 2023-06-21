package com.andree.panjaitan.parkeebe.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCheckout {

    @NotEmpty(message = "code is required")
    private String code;

    @NotEmpty(message = "front picture is required")
    @JsonProperty("front_picture")
    private String frontPicture;

    @NotEmpty(message = "driver picture is required")
    @JsonProperty("driver_picture")
    private String driverPicture;
}
