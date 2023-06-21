package com.andree.panjaitan.parkeebe.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findAllByDeletedAtIsNull();

    @Query(
            value = """
                    select
                    	lc2.capacity,
                    	coalesce(r.booked_count,
                    	0) as booked_count,
                    	vt.name as vehicle_type,
                    	vt.id as vehicle_type_id,
                    	SUM(lc2.capacity - coalesce(r.booked_count, 0)) available_capacity
                    from
                    	vehicle_type as vt
                    left
                    join (
                    	select
                    		lc.vehicle_type_id,
                    		count(*) as booked_count
                    	from
                    		location as l
                    	inner join location_capacity lc on
                    		l.id = lc.location_id
                    	inner join orders as o on
                    		o.location_id = l.id
                    	where
                    		l.id = :locationId
                    		and o.exit_at is null
                    	group by
                    		vehicle_type_id,
                    		lc.capacity) as r on
                    	r.vehicle_type_id = vehicle_type_id
                    	inner join location_capacity lc2 
                    	on lc2.vehicle_type_id = vt.id and lc2.location_id  = :locationId
                    	group by lc2.capacity, vehicle_type, lc2.vehicle_type_id, r.booked_count, vt.id
                     """, nativeQuery = true
    )
    List<BookedInfoLocation> countUsedLocation(UUID locationId);

    @Query(
            value = """
                    select
                    	lc2.capacity,
                    	coalesce(r.booked_count,
                    	0) as booked_count,
                    	vt.name as vehicle_type,
                    	vt.id as vehicle_type_id,
                    	SUM(lc2.capacity - coalesce(r.booked_count, 0)) available_capacity
                    from
                    	vehicle_type as vt
                    left
                    join (
                    	select
                    		lc.vehicle_type_id,
                    		count(*) as booked_count
                    	from
                    		location as l
                    	inner join location_capacity lc on
                    		l.id = lc.location_id
                    	inner join orders as o on
                    		o.location_id = l.id
                    	where
                    		l.id = :locationId
                    		and o.exit_at is null
                    	group by
                    		vehicle_type_id,
                    		lc.capacity) as r on
                    	r.vehicle_type_id = vehicle_type_id
                    	inner join location_capacity lc2
                    	on lc2.vehicle_type_id = vt.id and lc2.location_id  = :locationId
                    	where lc2.vehicle_type_id = :vehicleTypeID
                    	group by lc2.capacity, vehicle_type, lc2.vehicle_type_id, r.booked_count, vt.id
                     """, nativeQuery = true
    )
    Optional<BookedInfoLocation> countUsedLocationByVehicleTypeID(UUID locationId, UUID vehicleTypeID);


}
