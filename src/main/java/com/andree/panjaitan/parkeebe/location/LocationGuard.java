package com.andree.panjaitan.parkeebe.location;

import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.andree.panjaitan.parkeebe.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocationGuard extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @JsonProperty("entry_time")
    LocalDateTime entryTime;

    @JsonProperty("exit_time")
    LocalDateTime exitTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "location_id")
    @JsonBackReference
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "guard_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer", "tokens", "authorities"})
    private User guard;
}
