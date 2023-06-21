package com.andree.panjaitan.parkeebe.location;

import com.andree.panjaitan.parkeebe.order.OrderRepository;
import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.user.User;
import com.andree.panjaitan.parkeebe.vehicle.VehicleTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationGuardRepository locationGuardRepository;
    private final LocationCapacityRepository locationCapacityRepository;
    private final OrderRepository orderRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    public List<Location> getAllLocation() {
        return locationRepository.findAllByDeletedAtIsNull();
    }

    public Location findByID(UUID uuid) {
        return locationRepository.findById(uuid)
                .orElseThrow(() -> new ErrorAppException(CodeError
                        .NOT_FOUND.getCodeError(), "location not found"));
    }

    public ResponseCapacityLocation getCapacityLoan(UUID uuid) {
        var dataCount = locationRepository.countUsedLocation(uuid);
        List<BookedLocation> locations = new ArrayList<>();
        dataCount.forEach((it) -> {
            locations.add(BookedLocation
                    .builder()
                    .capacity(it.getCapacity())
                    .availableCapacity(it.getAvailable_capacity())
                    .bookedCount(it.getBooked_count())
                    .vehicleType(it.getVehicle_type())
                    .vehicleTypeId(UUID.fromString(it.getVehicle_type_id()))
                    .build());
        });
        return ResponseCapacityLocation
                .builder()
                .info(locations)
                .build();
    }

    public void checkInGuardLocation(User user, UUID locationID) {
        var location = locationRepository.findById(locationID).orElseThrow(() -> new ErrorAppException(CodeError
                .NOT_FOUND.getCodeError(), "location not found"));
        var locationActive = locationGuardRepository.findByGuard_IdAndExitTimeIsNull(user.getId()).orElse(null);
        if (locationActive != null)
            throw new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "user already guard one area");
        var guard = LocationGuard
                .builder()
                .guard(user)
                .location(location)
                .entryTime(LocalDateTime.now())
                .build();

        locationGuardRepository.save(guard);
    }

    public void checkOutGuardLocation(User user, UUID locationID) {
        var guards = locationGuardRepository.findAllByLocationId(locationID);
        guards.forEach((g) -> {
            g.setExitTime(LocalDateTime.now());
        });
        locationGuardRepository.saveAll(guards);
    }

    @Transactional
    public void createLocation(LocationRequest request, User user) {
        var code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        var location = Location
                .builder()
                .address(request.getAddress())
                .city(request.getCity())
                .name(request.getName())
                .codeArea(code)
                .build();
        locationRepository.save(location);
        List<LocationCapacity> locationCapacities = new ArrayList<>();
        request.getCapacities().forEach((c) -> {
            var vecType = vehicleTypeRepository.findById(UUID.fromString(c.getVehicleID()))
                    .orElseThrow(() ->
                            new ErrorAppException(
                                    CodeError.NOT_FOUND.getCodeError(),
                                    "vehicle type not found"));
            locationCapacities.add(LocationCapacity
                    .builder()
                    .location(location)
                    .capacity(c.getCapacity())
                    .vehicleType(vecType)
                    .build());
        });

        locationCapacityRepository.saveAll(locationCapacities);
    }
}
