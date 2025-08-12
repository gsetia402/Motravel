package org.moto.motravel.repository;

import org.moto.motravel.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Find vehicles by availability
    List<Vehicle> findByAvailability(Boolean availability);
    
    // Custom query to find vehicles within a certain radius using Haversine formula
    @Query(value = 
        "SELECT * FROM vehicles v WHERE " +
        "(6371 * acos(cos(radians(:latitude)) * cos(radians(v.latitude)) * " +
        "cos(radians(v.longitude) - radians(:longitude)) + " +
        "sin(radians(:latitude)) * sin(radians(v.latitude)))) < :radius " +
        "AND v.availability = true", 
        nativeQuery = true)
    List<Vehicle> findVehiclesNearLocation(
        @Param("latitude") Double latitude, 
        @Param("longitude") Double longitude, 
        @Param("radius") Double radius
    );
}
