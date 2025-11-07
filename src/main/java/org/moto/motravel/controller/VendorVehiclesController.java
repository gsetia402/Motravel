package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.User;
import org.moto.motravel.model.Vehicle;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.repository.UserRepository;
import org.moto.motravel.repository.VehicleRepository;
import org.moto.motravel.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vendor/vehicles")
@Tag(name = "Vendor - Vehicles", description = "Vendor APIs to manage own vehicles")
@PreAuthorize("hasAnyAuthority('ROLE_VENDOR','ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class VendorVehiclesController {

    @Autowired private VehicleService vehicleService;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private UserRepository userRepository;

    private String currentVendorId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).map(User::getVendorId).orElse(null);
    }

    @GetMapping
    @Operation(summary = "List my vehicles (vendor) or vehicles by vendorId (admin)")
    public ResponseEntity<List<Vehicle>> list(@RequestParam(required = false) String vendorId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        String vid = isAdmin ? (vendorId != null ? vendorId : currentVendorId()) : currentVendorId();
        if (vid == null && !isAdmin) return ResponseEntity.status(403).<List<Vehicle>>build();
        List<Vehicle> list = (vid == null) ? vehicleService.getAllVehicles() : vehicleRepository.findByVendorId(vid);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Operation(summary = "Create a vehicle for the current vendor")
    public ResponseEntity<?> create(@Valid @RequestBody Vehicle vehicle) {
        String vid = currentVendorId();
        if (vid == null) return ResponseEntity.status(403).body(new MessageResponse("No vendor assigned to user"));
        vehicle.setVendorId(vid);
        Vehicle saved = vehicleService.saveVehicle(vehicle);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update my vehicle")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Vehicle updates) {
        String vid = currentVendorId();
        var opt = vehicleRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Vehicle not found or not owned by vendor"));
        }
        var v = opt.get();
        if (v.getVendorId() == null || !v.getVendorId().equals(vid)) {
            return ResponseEntity.status(404).body(new MessageResponse("Vehicle not found or not owned by vendor"));
        }
        v.setModel(updates.getModel());
        v.setBrand(updates.getBrand());
        v.setType(updates.getType());
        v.setLatitude(updates.getLatitude());
        v.setLongitude(updates.getLongitude());
        v.setHourlyPrice(updates.getHourlyPrice());
        v.setImageUrl(updates.getImageUrl());
        v.setAvailability(updates.getAvailability());
        return ResponseEntity.ok(vehicleService.saveVehicle(v));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete my vehicle")
    public ResponseEntity<?> delete(@PathVariable String id) {
        String vid = currentVendorId();
        var opt = vehicleRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Vehicle not found or not owned by vendor"));
        }
        var v = opt.get();
        if (v.getVendorId() == null || !v.getVendorId().equals(vid)) {
            return ResponseEntity.status(404).body(new MessageResponse("Vehicle not found or not owned by vendor"));
        }
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(new MessageResponse("Vehicle deleted"));
    }
}
