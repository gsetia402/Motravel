package org.moto.motravel.repository;

import org.moto.motravel.model.AdventureType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdventureTypeRepository extends MongoRepository<AdventureType, String> {
    Optional<AdventureType> findByName(String name);
    boolean existsByName(String name);
    List<AdventureType> findAllByOrderByNameAsc();
    List<AdventureType> findByNameContainingIgnoreCaseOrderByNameAsc(String searchTerm);
}
