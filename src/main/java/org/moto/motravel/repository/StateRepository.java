package org.moto.motravel.repository;

import org.moto.motravel.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
    Optional<State> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT s FROM State s ORDER BY s.name ASC")
    List<State> findAllOrderByName();
    
    @Query("SELECT s FROM State s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY s.name ASC")
    List<State> findByNameContainingIgnoreCase(String searchTerm);
}
