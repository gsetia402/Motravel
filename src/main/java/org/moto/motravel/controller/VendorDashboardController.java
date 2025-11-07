package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.moto.motravel.model.User;
import org.moto.motravel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vendor/dashboard")
@Tag(name = "Vendor - Dashboard", description = "Vendor dashboard APIs: summary and bookings")
@PreAuthorize("hasAnyAuthority('ROLE_VENDOR','ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class VendorDashboardController {

    @Autowired private UserRepository userRepository;
    @Autowired private VendorRepository vendorRepository;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private TourPackageRepository tourPackageRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private TourBookingRepository tourBookingRepository;

    private String currentVendorId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).map(User::getVendorId).orElse(null);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary for current vendor")
    public ResponseEntity<?> summary() {
        String vendorId = currentVendorId();
        if (vendorId == null) return ResponseEntity.status(403).body(Map.of("error","No vendor assigned to user"));
        var vendor = vendorRepository.findById(vendorId).orElse(null);
        if (vendor == null) return ResponseEntity.status(404).body(Map.of("error","Vendor not found"));
        long vehicles = vehicleRepository.findByVendorId(vendorId).size();
        long tours = tourPackageRepository.findByVendorId(vendorId).size();
        long vehicleBookings = bookingRepository.findByVendorId(vendorId).size();
        long tourBookings = tourBookingRepository.findByVendorId(vendorId).size();
        return ResponseEntity.ok(Map.of(
                "vendor", vendor,
                "department", vendor.getDepartment(),
                "vehiclesCount", vehicles,
                "toursCount", tours,
                "vehicleBookingsCount", vehicleBookings,
                "tourBookingsCount", tourBookings
        ));
    }

    @GetMapping("/bookings")
    @Operation(summary = "List bookings for current vendor based on department")
    public ResponseEntity<?> bookings() {
        String vendorId = currentVendorId();
        if (vendorId == null) return ResponseEntity.status(403).body(Map.of("error","No vendor assigned to user"));
        var vendor = vendorRepository.findById(vendorId).orElse(null);
        if (vendor == null) return ResponseEntity.status(404).body(Map.of("error","Vendor not found"));
        String dept = vendor.getDepartment() != null ? vendor.getDepartment().toUpperCase() : "TOUR";
        if ("VEHICLE".equals(dept)) {
            return ResponseEntity.ok(bookingRepository.findByVendorId(vendorId));
        } else {
            return ResponseEntity.ok(tourBookingRepository.findByVendorId(vendorId));
        }
    }
}
