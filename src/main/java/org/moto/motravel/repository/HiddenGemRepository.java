package org.moto.motravel.repository;

import org.moto.motravel.model.HiddenGem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiddenGemRepository extends JpaRepository<HiddenGem, Long> {
    
    // Find by state
    Page<HiddenGem> findByStateId(Long stateId, Pageable pageable);
    
    // Find by adventure types
    @Query("SELECT DISTINCT h FROM HiddenGem h JOIN h.adventureTypes a WHERE a.id IN :adventureTypeIds")
    Page<HiddenGem> findByAdventureTypeIds(@Param("adventureTypeIds") List<Long> adventureTypeIds, Pageable pageable);
    
    // Find by state and adventure types
    @Query("SELECT DISTINCT h FROM HiddenGem h JOIN h.adventureTypes a WHERE h.state.id = :stateId AND a.id IN :adventureTypeIds")
    Page<HiddenGem> findByStateIdAndAdventureTypeIds(@Param("stateId") Long stateId, @Param("adventureTypeIds") List<Long> adventureTypeIds, Pageable pageable);
    
    // Search by name or description
    @Query("SELECT h FROM HiddenGem h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(h.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<HiddenGem> findByNameOrDescriptionContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Complex search with all filters - simplified to avoid Hibernate parsing issues
    @Query("SELECT DISTINCT h FROM HiddenGem h LEFT JOIN h.adventureTypes a WHERE " +
           "(:stateId IS NULL OR h.state.id = :stateId) AND " +
           "(:adventureTypeIds IS NULL OR a.id IN :adventureTypeIds) AND " +
           "(:searchTerm IS NULL OR " +
           "UPPER(h.name) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
           "UPPER(h.description) LIKE UPPER(CONCAT('%', :searchTerm, '%')))")
    Page<HiddenGem> findWithFilters(@Param("stateId") Long stateId, 
                                   @Param("adventureTypeIds") List<Long> adventureTypeIds, 
                                   @Param("searchTerm") String searchTerm, 
                                   Pageable pageable);
    
    // Find nearby gems within radius (in kilometers)
    @Query("SELECT h FROM HiddenGem h WHERE " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(h.latitude)))) <= :radius")
    List<HiddenGem> findNearbyGems(@Param("latitude") Double latitude, 
                                  @Param("longitude") Double longitude, 
                                  @Param("radius") Double radius);
    
    // Count by state
    long countByStateId(Long stateId);
    
    // Count by adventure type
    @Query("SELECT COUNT(DISTINCT h) FROM HiddenGem h JOIN h.adventureTypes a WHERE a.id = :adventureTypeId")
    long countByAdventureTypeId(@Param("adventureTypeId") Long adventureTypeId);
}
