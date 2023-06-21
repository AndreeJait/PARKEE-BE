package com.andree.panjaitan.parkeebe.location;

import com.andree.panjaitan.parkeebe.shared.BlankResponse;
import com.andree.panjaitan.parkeebe.shared.SuccessResponse;
import com.andree.panjaitan.parkeebe.utils.PrincipalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/location")
@Tag(name = "Location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService service;
    private final PrincipalUtils principalUtils;

    @GetMapping("/all")
    public SuccessResponse<List<Location>> getAllLocation() {
        return new SuccessResponse<>(service.getAllLocation());
    }

    @GetMapping("/{location_id}")
    public SuccessResponse<Location> findByLocationID(@PathVariable("location_id") String locationID) {
        return new SuccessResponse<>(service.findByID(UUID.fromString(locationID)));
    }

    @GetMapping("/{location_id}/capacity")
    public SuccessResponse<ResponseCapacityLocation> getCapacityLocation(@PathVariable("location_id") String locationID) {
        return new SuccessResponse<>(service.getCapacityLoan(UUID.fromString(locationID)));
    }

    @PostMapping("/checkin-guard")
    public SuccessResponse<BlankResponse> checkInGuardLocation(Principal principal, @RequestBody RequestCheckInOutLocation request){
        service.checkInGuardLocation(principalUtils.getUser(principal), UUID.fromString(request.getLocationId()));
        return new SuccessResponse<>(new BlankResponse());
    }

    @PostMapping("/checkout-guard")
    public SuccessResponse<BlankResponse> checkOutGuardLocation(Principal principal, @RequestBody RequestCheckInOutLocation request){
        service.checkOutGuardLocation(principalUtils.getUser(principal), UUID.fromString(request.getLocationId()));
        return new SuccessResponse<>(new BlankResponse());
    }

    @PostMapping("")
    public SuccessResponse<BlankResponse> createLocation(@RequestBody() LocationRequest request, Principal principal){
        service.createLocation(request, principalUtils.getUser(principal));
        return new SuccessResponse<>(new BlankResponse());
    }
}
