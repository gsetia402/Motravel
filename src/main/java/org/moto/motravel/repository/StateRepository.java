package org.moto.motravel.repository;

import org.moto.motravel.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends MongoRepository<State, String> {
    Optional<State> findByName(String name);
    boolean existsByName(String name);
    List<State> findAllByOrderByNameAsc();
    List<State> findByNameContainingIgnoreCaseOrderByNameAsc(String searchTerm);
}
