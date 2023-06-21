package com.andree.panjaitan.parkeebe.location;

import java.util.UUID;

public interface BookedInfoLocation {
    String getVehicle_type();
    String getVehicle_type_id();
    Integer getBooked_count();
    Integer getCapacity();

    Integer getAvailable_capacity();
}
