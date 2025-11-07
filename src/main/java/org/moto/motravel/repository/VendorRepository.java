package org.moto.motravel.repository;

import org.moto.motravel.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {
    java.util.List<Vendor> findByStatus(String status);
}
