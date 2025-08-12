package org.moto.motravel.service;

import org.moto.motravel.model.Booking;
import org.moto.motravel.model.Vehicle;
import org.moto.motravel.repository.BookingRepository;
import org.moto.motravel.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Get bookings by user ID
     */
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    /**
     * Get bookings by vehicle ID
     */
    public List<Booking> getBookingsByVehicleId(Long vehicleId) {
        return bookingRepository.findByVehicleId(vehicleId);
    }

    /**
     * Create a new booking
     */
    @Transactional
    public Booking createBooking(Booking booking) {
        // Check if vehicle is available for the requested time period
        boolean isVehicleBooked = bookingRepository.isVehicleBookedInTimeRange(
                booking.getVehicleId(), booking.getStartTime(), booking.getEndTime());

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
        
        return bookingRepository.save(booking);
    }

    /**
     * Update booking status
     */
    @Transactional
    public Booking updateBookingStatus(Long bookingId, String status) {
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
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        return updateBookingStatus(bookingId, "CANCELLED");
    }

    /**
     * Check if a vehicle is available for booking in a specific time range
     */
    public boolean isVehicleAvailableForBooking(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return !bookingRepository.isVehicleBookedInTimeRange(vehicleId, startTime, endTime);
    }
}
