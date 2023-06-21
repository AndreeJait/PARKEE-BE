package com.andree.panjaitan.parkeebe.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByVehiclePlatNumberAndExitAtNull(String platNumber);
    Optional<Order> findByVehiclePlatNumber(String platNumber);

    Optional<Order> findByUniqueCodeAndExitAtNull(String uniqueCode);
}
