package com.andree.panjaitan.parkeebe.location;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @JsonProperty("code_area")
    @Column(unique = true)
    private String codeArea;

    private String address;

    private String city;

    @JsonProperty("location_guard")
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    public List<LocationGuard> locationGuard;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonManagedReference
    public List<LocationCapacity> capacities;

}
