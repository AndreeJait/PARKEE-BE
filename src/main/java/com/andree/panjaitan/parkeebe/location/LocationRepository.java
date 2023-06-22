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
                                        	lc.capacity,
                                        	coalesce(booked_count,
                                        	0) as booked_count,
                                        	vt.name as vehicle_type,
                                        	vt.id as vehicle_type_id,
                                        	(lc.capacity - coalesce(r.booked_count,
                                        	0)) available_capacity
                                        from
                                        	vehicle_type as vt
                                        left
                                                            join (
                                        	select
                                        		v.vehicle_type_id,
                                        		l.id ,
                                        		count(*) as booked_count
                                        	from
                                        		orders o
                                        	inner join "location" l on
                                        		l.id = o.location_id
                                        	inner join vehicle v on
                                        		o.vehicle_plat_number = v.plat_number
                                        	inner join vehicle_type vt on
                                        		vt.id = v.vehicle_type_id
                                        	where
                                        		o.exit_at is null
                                        	group by
                                        		l.id,
                                        		v.vehicle_type_id) as r on
                                        	r.vehicle_type_id = vt.id
                                        inner join location_capacity lc
                                            on
                                        	lc.vehicle_type_id = vt.id
                                        	and lc.location_id = :locationId
                     """, nativeQuery = true
    )
    List<BookedInfoLocation> countUsedLocation(UUID locationId);

    @Query(
            value = """
                    select
                                        	lc.capacity,
                                        	coalesce(booked_count,
                                        	0) as booked_count,
                                        	vt.name as vehicle_type,
                                        	vt.id as vehicle_type_id,
                                        	(lc.capacity - coalesce(r.booked_count,
                                        	0)) available_capacity
                                        from
                                        	vehicle_type as vt
                                        left
                                                            join (
                                        	select
                                        		v.vehicle_type_id,
                                        		l.id ,
                                        		count(*) as booked_count
                                        	from
                                        		orders o
                                        	inner join "location" l on
                                        		l.id = o.location_id
                                        	inner join vehicle v on
                                        		o.vehicle_plat_number = v.plat_number
                                        	inner join vehicle_type vt on
                                        		vt.id = v.vehicle_type_id
                                        	where
                                        		o.exit_at is null
                                        	group by
                                        		l.id,
                                        		v.vehicle_type_id) as r on
                                        	r.vehicle_type_id = vt.id
                                        inner join location_capacity lc
                                            on
                                        	lc.vehicle_type_id = vt.id
                                        	and lc.location_id = :locationId
                                        where lc.vehicle_type_id = :vehicleTypeID
                    """, nativeQuery = true
    )
    Optional<BookedInfoLocation> countUsedLocationByVehicleTypeID(UUID locationId, UUID vehicleTypeID);


}
