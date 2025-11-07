package org.moto.motravel.service;

import org.moto.motravel.model.HiddenGem;
import org.moto.motravel.repository.AdventureTypeRepository;
import org.moto.motravel.repository.HiddenGemRepository;
import org.moto.motravel.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HiddenGemService {

    @Autowired
    private HiddenGemRepository hiddenGemRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private AdventureTypeRepository adventureTypeRepository;

    /**
     * Get all hidden gems with pagination and sorting
     */
    public Page<HiddenGem> getAllHiddenGems(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return hiddenGemRepository.findAll(pageable);
    }

    /**
     * Get hidden gem by ID
     */
    public Optional<HiddenGem> getHiddenGemById(String id) {
        return hiddenGemRepository.findById(id);
    }

    /**
     * Search hidden gems with filters
     */
    public Page<HiddenGem> searchHiddenGems(String stateId, List<String> adventureTypeIds, String searchTerm, 
                                          int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (adventureTypeIds != null && adventureTypeIds.isEmpty()) adventureTypeIds = null;
        if (searchTerm != null && searchTerm.isBlank()) searchTerm = null; // reserved for future text search

        if (stateId != null) {
            return hiddenGemRepository.findByStateId(stateId, pageable);
        } else if (adventureTypeIds != null) {
            return hiddenGemRepository.findByAdventureTypeIdsIn(adventureTypeIds, pageable);
        }
        return hiddenGemRepository.findAll(pageable);
    }

    /**
     * Find nearby hidden gems
     */
    public List<HiddenGem> findNearbyGems(Double latitude, Double longitude, Double radiusKm) {
        // TODO: implement geo search using MongoTemplate.geoNear; return all for now
        return hiddenGemRepository.findAll();
    }

    /**
     * Create a new hidden gem
     */
    public HiddenGem createHiddenGem(HiddenGem hiddenGem) {
        if (hiddenGem.getStateId() == null || stateRepository.findById(hiddenGem.getStateId()).isEmpty()) {
            throw new IllegalArgumentException("State not found with id: " + hiddenGem.getStateId());
        }
        if (hiddenGem.getAdventureTypeIds() != null && !hiddenGem.getAdventureTypeIds().isEmpty()) {
            Set<String> validIds = new HashSet<>();
            for (String atId : hiddenGem.getAdventureTypeIds()) {
                if (adventureTypeRepository.findById(atId).isPresent()) validIds.add(atId);
                else throw new IllegalArgumentException("Adventure type not found with id: " + atId);
            }
            hiddenGem.setAdventureTypeIds(validIds);
        }
        // derive GeoJSON location if coords present
        if (hiddenGem.getLongitude() != null && hiddenGem.getLatitude() != null) {
            hiddenGem.setLocation(new org.springframework.data.mongodb.core.geo.GeoJsonPoint(hiddenGem.getLongitude(), hiddenGem.getLatitude()));
        }
        return hiddenGemRepository.save(hiddenGem);
    }

    /**
     * Update an existing hidden gem
     */
    public HiddenGem updateHiddenGem(String id, HiddenGem hiddenGemDetails) {
        HiddenGem hiddenGem = hiddenGemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hidden gem not found with id: " + id));

        // Update basic fields
        hiddenGem.setName(hiddenGemDetails.getName());
        hiddenGem.setDescription(hiddenGemDetails.getDescription());
        hiddenGem.setLatitude(hiddenGemDetails.getLatitude());
        hiddenGem.setLongitude(hiddenGemDetails.getLongitude());
        hiddenGem.setNearestCity(hiddenGemDetails.getNearestCity());
        hiddenGem.setBestTimeToVisit(hiddenGemDetails.getBestTimeToVisit());
        hiddenGem.setDifficultyLevel(hiddenGemDetails.getDifficultyLevel());
        hiddenGem.setCostRange(hiddenGemDetails.getCostRange());
        hiddenGem.setImageUrls(hiddenGemDetails.getImageUrls());

        // Update state if provided
        if (hiddenGemDetails.getStateId() != null) {
            stateRepository.findById(hiddenGemDetails.getStateId())
                    .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + hiddenGemDetails.getStateId()));
            hiddenGem.setStateId(hiddenGemDetails.getStateId());
        }

        // Update adventure types if provided
        if (hiddenGemDetails.getAdventureTypeIds() != null && !hiddenGemDetails.getAdventureTypeIds().isEmpty()) {
            Set<String> validated = new HashSet<>();
            for (String atId : hiddenGemDetails.getAdventureTypeIds()) {
                adventureTypeRepository.findById(atId)
                        .orElseThrow(() -> new IllegalArgumentException("Adventure type not found with id: " + atId));
                validated.add(atId);
            }
            hiddenGem.setAdventureTypeIds(validated);
        }

        if (hiddenGem.getLongitude() != null && hiddenGem.getLatitude() != null) {
            hiddenGem.setLocation(new org.springframework.data.mongodb.core.geo.GeoJsonPoint(hiddenGem.getLongitude(), hiddenGem.getLatitude()));
        }

        return hiddenGemRepository.save(hiddenGem);
    }

    /**
     * Delete a hidden gem
     */
    public void deleteHiddenGem(String id) {
        HiddenGem hiddenGem = hiddenGemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hidden gem not found with id: " + id));
        hiddenGemRepository.delete(hiddenGem);
    }

    /**
     * Get hidden gems by state
     */
    public Page<HiddenGem> getHiddenGemsByState(String stateId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return hiddenGemRepository.findByStateId(stateId, pageable);
    }

    /**
     * Get hidden gems by adventure types
     */
    public Page<HiddenGem> getHiddenGemsByAdventureTypes(List<String> adventureTypeIds, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return hiddenGemRepository.findByAdventureTypeIdsIn(adventureTypeIds, pageable);
    }

    /**
     * Get statistics
     */
    public long getTotalHiddenGemsCount() {
        return hiddenGemRepository.count();
    }

    public long getHiddenGemsCountByState(String stateId) {
        return hiddenGemRepository.countByStateId(stateId);
    }

    public long getHiddenGemsCountByAdventureType(String adventureTypeId) {
        return hiddenGemRepository.countByAdventureTypeIdsContains(adventureTypeId);
    }

    /**
     * Validate sort field
     */
    public String validateSortField(String sortBy) {
        // Define allowed sort fields
        Set<String> allowedSortFields = Set.of(
            "id", "name", "createdAt", "updatedAt",
            "nearestCity", "bestTimeToVisit", "difficultyLevel"
        );
        
        if (sortBy == null || !allowedSortFields.contains(sortBy)) {
            return "createdAt"; // Default sort field
        }
        return sortBy;
    }

    /**
     * Validate sort direction
     */
    public String validateSortDirection(String sortDirection) {
        if (sortDirection == null || 
            (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc"))) {
            return "desc"; // Default sort direction
        }
        return sortDirection.toLowerCase();
    }
}
