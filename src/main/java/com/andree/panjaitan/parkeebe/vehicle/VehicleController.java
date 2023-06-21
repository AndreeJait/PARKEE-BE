package com.andree.panjaitan.parkeebe.vehicle;

import com.andree.panjaitan.parkeebe.shared.BlankResponse;
import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import com.andree.panjaitan.parkeebe.utils.PrincipalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicle")
@Tag(name = "Vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService service;
    private final PrincipalUtils principalUtils;

    @GetMapping("/types")
    public SuccessResponse<List<VehicleType>> getAllVehicleType() {
        return new SuccessResponse<>(service.getAllVehicleType());
    }

    @GetMapping("/{location_id}/types")
    public SuccessResponse<List<VehicleType>> getVehicleByLocation(@PathVariable("location_id") String locationId) {
        UUID locationUuid = UUID.fromString(locationId);
        return new SuccessResponse<List<VehicleType>>(service.getAllVehicleTypeInLocation(locationUuid));
    }

    @PostMapping("/type")
    public SuccessResponse<BlankResponse> createNewVehicleType(@Valid @RequestBody VehicleTypeRequest request) {
        service.createNewVehicleType(request);
        return new SuccessResponse<>(new BlankResponse(), "success to create new vehicle type");
    }

    @DeleteMapping("/{vehicle_type_id}")
    public SuccessResponse<BlankResponse> deleteVehicleType(@PathVariable("vehicle_type_id") String vehicleTypeID, Principal principal){
        service.deleteVehicleType(UUID.fromString(vehicleTypeID), principalUtils.getUser(principal));
        return new SuccessResponse<>(new BlankResponse(), "success to delete data");
    }
}
