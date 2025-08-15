package org.moto.motravel.repository;

import org.moto.motravel.model.AdventureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdventureTypeRepository extends JpaRepository<AdventureType, Long> {
    
    Optional<AdventureType> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT a FROM AdventureType a ORDER BY a.name ASC")
    List<AdventureType> findAllOrderByName();
    
    @Query("SELECT a FROM AdventureType a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.name ASC")
    List<AdventureType> findByNameContainingIgnoreCase(String searchTerm);
}
