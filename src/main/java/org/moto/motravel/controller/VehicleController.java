package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.Vehicle;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle", description = "Vehicle management APIs")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get all vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available vehicles")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find vehicles near a location within a specified radius (in km)")
    public ResponseEntity<List<Vehicle>> findVehiclesNearLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        List<Vehicle> vehicles = vehicleService.findVehiclesNearLocation(latitude, longitude, radius);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicleDetails) {
        return vehicleService.getVehicleById(id)
                .map(vehicle -> {
                    vehicle.setModel(vehicleDetails.getModel());
                    vehicle.setBrand(vehicleDetails.getBrand());
                    vehicle.setType(vehicleDetails.getType());
                    vehicle.setLatitude(vehicleDetails.getLatitude());
                    vehicle.setLongitude(vehicleDetails.getLongitude());
                    vehicle.setHourlyPrice(vehicleDetails.getHourlyPrice());
                    vehicle.setImageUrl(vehicleDetails.getImageUrl());
                    vehicle.setAvailability(vehicleDetails.getAvailability());
                    
                    Vehicle updatedVehicle = vehicleService.saveVehicle(vehicle);
                    return ResponseEntity.ok(updatedVehicle);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update vehicle availability", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateVehicleAvailability(
            @PathVariable Long id,
            @RequestParam Boolean availability) {
        
        boolean updated = vehicleService.updateVehicleAvailability(id, availability);
        
        if (updated) {
            return ResponseEntity.ok(new MessageResponse("Vehicle availability updated successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a vehicle", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(vehicle -> {
                    vehicleService.deleteVehicle(id);
                    return ResponseEntity.ok(new MessageResponse("Vehicle deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
