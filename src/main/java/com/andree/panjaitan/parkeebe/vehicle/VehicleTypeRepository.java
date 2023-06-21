package com.andree.panjaitan.parkeebe.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, UUID> {
    @Query(value = """
            select vt from VehicleType vt inner 
            join LocationCapacity lc on vt.id = lc.vehicleType.id 
            WHERE lc.location.id = :locationId
            and vt.deletedAt is null""")
    Set<VehicleType> findAllByLocationID(UUID locationId);

    Set<VehicleType> findAllByDeletedAtIsNull();
}
