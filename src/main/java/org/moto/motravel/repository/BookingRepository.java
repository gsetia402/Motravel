package org.moto.motravel.repository;

import org.moto.motravel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find bookings by user ID
    List<Booking> findByUserId(Long userId);
    
    // Find bookings by vehicle ID
    List<Booking> findByVehicleId(Long vehicleId);
    
    // Find bookings by status
    List<Booking> findByStatus(String status);
    
    // Find bookings by user ID and status
    List<Booking> findByUserIdAndStatus(Long userId, String status);
    
    // Check if a vehicle is available for booking in a specific time range
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicleId = :vehicleId " +
           "AND b.status NOT IN ('CANCELLED') " +
           "AND ((b.startTime <= :endTime AND b.endTime >= :startTime))")
    boolean isVehicleBookedInTimeRange(
        @Param("vehicleId") Long vehicleId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
