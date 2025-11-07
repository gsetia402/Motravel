package org.moto.motravel.repository;

import org.moto.motravel.model.TourPackage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourPackageRepository extends MongoRepository<TourPackage, String> {
    java.util.List<TourPackage> findByVendorId(String vendorId);
}
