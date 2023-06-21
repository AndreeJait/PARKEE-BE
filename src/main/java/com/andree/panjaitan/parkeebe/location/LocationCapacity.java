package com.andree.panjaitan.parkeebe.location;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.andree.panjaitan.parkeebe.vehicle.Vehicle;
import com.andree.panjaitan.parkeebe.vehicle.VehicleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "location_capacity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class LocationCapacity extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "vehicle_type_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "location_id")
    @JsonBackReference
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Location location;

    private Integer capacity;

    @JsonProperty("price_per_hour")
    private BigInteger pricePerHour;
}
