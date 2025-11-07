package org.moto.motravel.service;

import org.moto.motravel.model.Vehicle;
import org.moto.motravel.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Get all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Get vehicle by ID
     */
    public Optional<Vehicle> getVehicleById(String id) {
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
    public List<Vehicle> findVehiclesNearLocation(Double latitude, Double longitude, Double radiusKm) {
        Point point = new Point(longitude, latitude);
        NearQuery near = NearQuery.near(point).maxDistance(new Distance(radiusKm, Metrics.KILOMETERS));
        return mongoTemplate.geoNear(near, Vehicle.class)
                .getContent()
                .stream()
                .map(hit -> hit.getContent())
                .toList();
    }

    /**
     * Save a new vehicle
     */
    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getLatitude() != null && vehicle.getLongitude() != null) {
            vehicle.setLocation(new GeoJsonPoint(vehicle.getLongitude(), vehicle.getLatitude()));
        }
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update vehicle availability
     */
    public boolean updateVehicleAvailability(String vehicleId, boolean availability) {
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
    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }
}
