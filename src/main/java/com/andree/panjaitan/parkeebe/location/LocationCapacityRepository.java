package com.andree.panjaitan.parkeebe.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationCapacityRepository extends JpaRepository<LocationCapacity, UUID> {
    Optional<LocationCapacity> findByVehicleTypeIdAndLocationIdAndDeletedAtIsNull(
            UUID vehicleTypeID,
            UUID locationId
    );
}
