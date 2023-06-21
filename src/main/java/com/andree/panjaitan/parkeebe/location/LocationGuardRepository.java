package com.andree.panjaitan.parkeebe.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationGuardRepository extends JpaRepository<LocationGuard, UUID> {
    List<LocationGuard> findAllByLocationId(UUID locationID);

    Optional<LocationGuard> findByGuard_IdAndExitTimeIsNull(UUID userID);
}
