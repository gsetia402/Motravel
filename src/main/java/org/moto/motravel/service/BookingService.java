package org.moto.motravel.service;

import org.moto.motravel.model.Booking;
import org.moto.motravel.model.Vehicle;
import org.moto.motravel.repository.BookingRepository;
import org.moto.motravel.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get booking by ID
     */
    public Optional<Booking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    /**
     * Get bookings by user ID
     */
    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    /**
     * Get bookings by vehicle ID
     */
    public List<Booking> getBookingsByVehicleId(String vehicleId) {
        return bookingRepository.findByVehicleId(vehicleId);
    }

    /**
     * Create a new booking
     */
    public Booking createBooking(Booking booking) {
        // Check if vehicle is available for the requested time period (exclude CANCELLED)
        boolean isVehicleBooked = bookingRepository.existsByVehicleIdAndStatusNotInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                booking.getVehicleId(), java.util.List.of("CANCELLED"), booking.getEndTime(), booking.getStartTime());

        if (isVehicleBooked) {
            throw new IllegalStateException("Vehicle is not available for the requested time period");
        }

        // Get vehicle to calculate total price
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(booking.getVehicleId());
        if (vehicleOpt.isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        Vehicle vehicle = vehicleOpt.get();
        
        // Check if vehicle is available
        if (!vehicle.getAvailability()) {
            throw new IllegalStateException("Vehicle is not available for booking");
        }

        // Calculate total price based on hourly rate and duration
        long hours = Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
        if (hours < 1) {
            hours = 1; // Minimum 1 hour
        }
        
        double totalPrice = vehicle.getHourlyPrice() * hours;
        booking.setTotalPrice(totalPrice);
        
        // Set initial status
        booking.setStatus("PENDING");
        // Set vendorId from vehicle
        booking.setVendorId(vehicle.getVendorId());
        
        return bookingRepository.save(booking);
    }

    /**
     * Update booking status
     */
    public Booking updateBookingStatus(String bookingId, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        booking.setStatus(status);
        
        return bookingRepository.save(booking);
    }

    /**
     * Cancel booking
     */
    public Booking cancelBooking(String bookingId) {
        return updateBookingStatus(bookingId, "CANCELLED");
    }

    /**
     * Check if a vehicle is available for booking in a specific time range
     */
    public boolean isVehicleAvailableForBooking(String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return !bookingRepository.existsByVehicleIdAndStatusNotInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                vehicleId, java.util.List.of("CANCELLED"), endTime, startTime);
    }
}
