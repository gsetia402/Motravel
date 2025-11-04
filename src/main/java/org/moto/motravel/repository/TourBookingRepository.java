package org.moto.motravel.repository;

import org.moto.motravel.model.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking, Long> {
    boolean existsByBookingId(String bookingId);

    @Query("SELECT COALESCE(SUM(b.adults + b.children), 0) FROM TourBooking b WHERE b.tourPackage.id = :packageId AND b.date = :date AND b.status IN ('PENDING','CONFIRMED')")
    int getBookedCountForDate(@Param("packageId") Long packageId, @Param("date") LocalDate date);

    java.util.List<TourBooking> findByUserId(Long userId);
}
