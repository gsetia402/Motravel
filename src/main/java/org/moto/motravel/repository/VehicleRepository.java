package org.moto.motravel.repository;

import org.moto.motravel.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByAvailability(Boolean availability);
    List<Vehicle> findByVendorId(String vendorId);
}
