package org.moto.motravel.service;

import org.moto.motravel.model.Vehicle;
import org.moto.motravel.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Get all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Get vehicle by ID
     */
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    /**
     * Get available vehicles
     */
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByAvailability(true);
    }

    /**
     * Find vehicles near a location within a specified radius (in km)
     */
    public List<Vehicle> findVehiclesNearLocation(Double latitude, Double longitude, Double radius) {
        return vehicleRepository.findVehiclesNearLocation(latitude, longitude, radius);
    }

    /**
     * Save a new vehicle
     */
    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update vehicle availability
     */
    @Transactional
    public boolean updateVehicleAvailability(Long vehicleId, boolean availability) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setAvailability(availability);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }

    /**
     * Delete a vehicle
     */
    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
