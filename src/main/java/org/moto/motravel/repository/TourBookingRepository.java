package org.moto.motravel.repository;

import org.moto.motravel.model.TourBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourBookingRepository extends MongoRepository<TourBooking, String> {
    boolean existsByBookingId(String bookingId);

    List<TourBooking> findByTourPackageIdAndDateAndStatusIn(String tourPackageId, LocalDate date, List<String> statuses);

    List<TourBooking> findByUserId(String userId);

    List<TourBooking> findByVendorId(String vendorId);
}
