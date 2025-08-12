package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.Booking;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking", description = "Booking management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings (Admin only)")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    // Check if the user is authorized to view this booking
                    if (isUserAuthorized(booking)) {
                        return ResponseEntity.ok(booking);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                new MessageResponse("You are not authorized to view this booking"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get bookings for the current user")
    public ResponseEntity<List<Booking>> getUserBookings() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // In a real application, you would get the user ID from the authenticated user
        // For simplicity, we're assuming the username is the user ID as a string
        Long userId = Long.parseLong(userDetails.getUsername());
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new booking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody Booking booking) {
        try {
            // Set the user ID from the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // In a real application, you would get the user ID from the authenticated user
            // For simplicity, we're assuming the username is the user ID as a string
            Long userId = Long.parseLong(userDetails.getUsername());
            booking.setUserId(userId);
            
            Booking createdBooking = bookingService.createBooking(booking);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update booking status (Admin only)")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        try {
            Booking updatedBooking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    // Check if the user is authorized to cancel this booking
                    if (isUserAuthorized(booking)) {
                        try {
                            Booking cancelledBooking = bookingService.cancelBooking(id);
                            return ResponseEntity.ok(cancelledBooking);
                        } catch (IllegalArgumentException e) {
                            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                new MessageResponse("You are not authorized to cancel this booking"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check-availability")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Check if a vehicle is available for booking in a specific time range")
    public ResponseEntity<?> checkVehicleAvailability(
            @RequestParam Long vehicleId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        
        boolean isAvailable = bookingService.isVehicleAvailableForBooking(vehicleId, startTime, endTime);
        
        if (isAvailable) {
            return ResponseEntity.ok(new MessageResponse("Vehicle is available for booking"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Vehicle is not available for the requested time period"));
        }
    }

    // Helper method to check if the current user is authorized to access a booking
    private boolean isUserAuthorized(Booking booking) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Admin can access any booking
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // User can only access their own bookings
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        
        return booking.getUserId().equals(userId);
    }
}
