package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.Vendor;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/vendors")
@Tag(name = "Admin - Vendors", description = "Admin APIs for managing vendors")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminVendorController {

    @Autowired private VendorRepository vendorRepository;
    @Autowired private TourPackageRepository tourPackageRepository;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private TourBookingRepository tourBookingRepository;

    @GetMapping
    @Operation(summary = "List all vendors")
    public List<Vendor> listVendors() {
        return vendorRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create vendor")
    public ResponseEntity<?> createVendor(@Valid @RequestBody Vendor vendor) {
        if (vendor.getStatus() == null) vendor.setStatus("ACTIVE");
        Vendor saved = vendorRepository.save(vendor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor details with related assets counts")
    public ResponseEntity<?> getVendor(@PathVariable Long id) {
        return vendorRepository.findById(id)
                .map(v -> {
                    Map<String,Object> dto = new HashMap<>();
                    dto.put("vendor", v);
                    dto.put("tourPackages", tourPackageRepository.findByVendorId(id));
                    dto.put("vehicles", vehicleRepository.findByVendorId(id));
                    // For bookings, we could add vendor-scoped queries later; for now return counts from in-memory filter
                    long vehicleBookings = bookingRepository.findAll().stream().filter(b -> id.equals(b.getVendorId())).count();
                    long tourBookings = tourBookingRepository.findAll().stream().filter(b -> id.equals(b.getVendorId())).count();
                    dto.put("vehicleBookingsCount", vehicleBookings);
                    dto.put("tourBookingsCount", tourBookings);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vendor")
    public ResponseEntity<?> updateVendor(@PathVariable Long id, @Valid @RequestBody Vendor updates) {
        return vendorRepository.findById(id)
                .map(v -> {
                    v.setName(updates.getName());
                    v.setEmail(updates.getEmail());
                    v.setPhone(updates.getPhone());
                    if (updates.getStatus() != null) v.setStatus(updates.getStatus());
                    return ResponseEntity.ok(vendorRepository.save(v));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate/Deactivate vendor")
    public ResponseEntity<?> setVendorStatus(@PathVariable Long id, @RequestParam String status) {
        return vendorRepository.findById(id)
                .map(v -> {
                    v.setStatus(status);
                    vendorRepository.save(v);
                    return ResponseEntity.ok(new MessageResponse("Vendor status updated"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
