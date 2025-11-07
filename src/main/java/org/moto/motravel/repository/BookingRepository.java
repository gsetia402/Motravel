package org.moto.motravel.repository;

import org.moto.motravel.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByVehicleId(String vehicleId);
    List<Booking> findByStatus(String status);
    List<Booking> findByUserIdAndStatus(String userId, String status);
    List<Booking> findByVendorId(String vendorId);

    boolean existsByVehicleIdAndStatusNotInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            String vehicleId,
            List<String> statusNotIn,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}
