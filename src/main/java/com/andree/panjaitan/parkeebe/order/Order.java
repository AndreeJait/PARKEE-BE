package com.andree.panjaitan.parkeebe.order;

import com.andree.panjaitan.parkeebe.vehicle.Vehicle;
import com.andree.panjaitan.parkeebe.location.Location;
import com.andree.panjaitan.parkeebe.shared.BaseEntity;
import com.andree.panjaitan.parkeebe.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity
{
    @Id
    @GeneratedValue
    private UUID id;

    @JsonProperty("entry_at")
    LocalDateTime entryAt;
    @JsonProperty("exit_at")
    LocalDateTime exitAt;

    @JsonProperty("unique_code")
    private String uniqueCode;

    @JsonProperty("entry_car_image")
    private String entryCarImage;
    @JsonProperty("entry_car_driver_image")
    private String entryCarDriverImage;

    @JsonProperty("exit_car_image")
    private String exitCarImage;

    @JsonProperty("ticket_link")
    private String ticketLink;

    @JsonProperty("entry_car_driver_image")
    private String exitCarDriverImage;

    @JsonProperty("price_per_hour")
    private BigInteger pricePerHour;

    @JsonProperty("total_price")
    private BigInteger totalPrice;

    @JsonProperty("total_hour")
    private int totalHour;

    @JsonProperty("dif_string")
    private String difString;

    @JsonProperty("is_paid")
    private boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "location_id")
    @JsonIgnoreProperties("locationGuard")
    @JsonBackReference
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "guard_id")
    @JsonBackReference
    private User guard;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "plat_number")
    @JsonBackReference
    private Vehicle vehicle;
}
