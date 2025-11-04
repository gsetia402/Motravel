package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.moto.motravel.model.TourBooking;
import org.moto.motravel.model.TourPackage;
import org.moto.motravel.service.TourService;
import org.moto.motravel.model.User;
import org.moto.motravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tours")
@Tag(name = "Tours", description = "Tour packages and booking APIs")
public class TourController {

    @Autowired
    private TourService tourService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all tour packages")
    public ResponseEntity<List<TourPackage>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tour package by ID")
    public ResponseEntity<?> getTourById(@PathVariable Long id) {
        TourPackage tour = tourService.getTourById(id);
        if (tour == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tour);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Check availability for a tour and date")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @Min(1) int guests
    ) {
        boolean available = tourService.checkAvailability(id, date, guests);
        return ResponseEntity.ok(Map.of("available", available));
    }

    @PostMapping("/{id}/book")
    @Operation(summary = "Create and confirm a booking for a tour")
    public ResponseEntity<?> bookTour(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @Min(1) int adults,
            @RequestParam(defaultValue = "0") @Min(0) int children,
            @RequestParam @NotBlank String contactName,
            @RequestParam @NotBlank @Email String contactEmail,
            @RequestParam @NotBlank String contactPhone
    ) {
        try {
            Long userId = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
                if (user != null) userId = user.getId();
            }

            TourBooking booking = tourService.createBooking(id, date, adults, children, contactName, contactEmail, contactPhone, userId);
            return ResponseEntity.ok(Map.of(
                    "bookingId", booking.getBookingId(),
                    "status", booking.getStatus(),
                    "totalPrice", booking.getTotalPrice(),
                    "tourId", booking.getTourPackage().getId(),
                    "date", booking.getDate()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get tour bookings for the current user")
    public ResponseEntity<List<TourBooking>> getMyTourBookings() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            if (user != null) return ResponseEntity.ok(tourService.getBookingsByUserId(user.getId()));
        }
        return ResponseEntity.ok(java.util.List.of());
    }
}
