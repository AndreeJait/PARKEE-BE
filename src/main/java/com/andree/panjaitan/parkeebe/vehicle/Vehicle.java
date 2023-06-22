package com.andree.panjaitan.parkeebe.vehicle;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends BaseEntity {
    @Id
    @Column(unique = true)
    @JsonProperty("plat_number")
    public String platNumber;

    @JsonProperty("total_parking")
    public long totalParking;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "vehicle_type_id")
    @JsonProperty("vehicle_type")
    public VehicleType vehicleType;
}
