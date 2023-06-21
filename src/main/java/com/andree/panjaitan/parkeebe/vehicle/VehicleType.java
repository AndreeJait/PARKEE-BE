package com.andree.panjaitan.parkeebe.vehicle;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vehicle_type")
@EqualsAndHashCode(callSuper = true)
public class VehicleType extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
}
