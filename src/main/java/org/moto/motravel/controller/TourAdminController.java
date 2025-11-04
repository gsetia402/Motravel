package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.TourBooking;
import org.moto.motravel.model.TourPackage;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.repository.TourBookingRepository;
import org.moto.motravel.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin - Tours", description = "Admin APIs for managing tours and tour bookings")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class TourAdminController {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    // --- Tour Packages CRUD ---

    @GetMapping("/tours")
    @Operation(summary = "List all tour packages")
    public List<TourPackage> listTours() {
        return tourPackageRepository.findAll();
    }

    @PostMapping("/tours")
    @Operation(summary = "Create a tour package")
    public ResponseEntity<?> createTour(@Valid @RequestBody TourPackage tour) {
        TourPackage saved = tourPackageRepository.save(tour);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/tours/{id}")
    @Operation(summary = "Update a tour package")
    public ResponseEntity<?> updateTour(@PathVariable Long id, @Valid @RequestBody TourPackage tour) {
        Optional<TourPackage> existing = tourPackageRepository.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        tour.setId(id);
        TourPackage saved = tourPackageRepository.save(tour);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/tours/{id}")
    @Operation(summary = "Delete a tour package")
    public ResponseEntity<?> deleteTour(@PathVariable Long id) {
        if (!tourPackageRepository.existsById(id)) return ResponseEntity.notFound().build();
        tourPackageRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Tour package deleted"));
    }

    // --- Tour Bookings management ---

    @GetMapping("/tour-bookings")
    @Operation(summary = "List all tour bookings")
    public List<TourBooking> listTourBookings() {
        return tourBookingRepository.findAll();
    }

    @GetMapping("/tour-bookings/{id}")
    @Operation(summary = "Get tour booking by id")
    public ResponseEntity<?> getTourBooking(@PathVariable Long id) {
        return tourBookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/tour-bookings/{id}/status")
    @Operation(summary = "Update tour booking status")
    public ResponseEntity<?> updateTourBookingStatus(@PathVariable Long id, @RequestParam String status) {
        return tourBookingRepository.findById(id)
                .map(b -> {
                    b.setStatus(status);
                    return ResponseEntity.ok(tourBookingRepository.save(b));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tour-bookings/{id}/cancel")
    @Operation(summary = "Cancel a tour booking")
    public ResponseEntity<?> cancelTourBooking(@PathVariable Long id) {
        return tourBookingRepository.findById(id)
                .map(b -> {
                    b.setStatus("CANCELLED");
                    return ResponseEntity.ok(tourBookingRepository.save(b));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
