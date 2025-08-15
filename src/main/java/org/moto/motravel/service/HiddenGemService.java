package org.moto.motravel.service;

import org.moto.motravel.model.AdventureType;
import org.moto.motravel.model.HiddenGem;
import org.moto.motravel.model.State;
import org.moto.motravel.repository.AdventureTypeRepository;
import org.moto.motravel.repository.HiddenGemRepository;
import org.moto.motravel.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
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
    public Optional<HiddenGem> getHiddenGemById(Long id) {
        return hiddenGemRepository.findById(id);
    }

    /**
     * Search hidden gems with filters
     */
    public Page<HiddenGem> searchHiddenGems(Long stateId, List<Long> adventureTypeIds, String searchTerm, 
                                          int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Clean up parameters to avoid PostgreSQL type issues
        if (searchTerm != null && searchTerm.trim().isEmpty()) {
            searchTerm = null;
        }
        if (adventureTypeIds != null && adventureTypeIds.isEmpty()) {
            adventureTypeIds = null;
        }

        // If no filters are applied, return all gems
        if (stateId == null && adventureTypeIds == null && searchTerm == null) {
            return hiddenGemRepository.findAll(pageable);
        }

        // Use the complex search query with cleaned parameters
        return hiddenGemRepository.findWithFilters(stateId, adventureTypeIds, searchTerm, pageable);
    }

    /**
     * Find nearby hidden gems
     */
    public List<HiddenGem> findNearbyGems(Double latitude, Double longitude, Double radiusKm) {
        if (radiusKm == null || radiusKm <= 0) {
            radiusKm = 50.0; // Default 50km radius
        }
        return hiddenGemRepository.findNearbyGems(latitude, longitude, radiusKm);
    }

    /**
     * Create a new hidden gem
     */
    public HiddenGem createHiddenGem(HiddenGem hiddenGem) {
        // Validate state exists
        State state = stateRepository.findById(hiddenGem.getState().getId())
                .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + hiddenGem.getState().getId()));
        hiddenGem.setState(state);

        // Validate and set adventure types
        Set<AdventureType> validatedAdventureTypes = new HashSet<>();
        for (AdventureType adventureType : hiddenGem.getAdventureTypes()) {
            AdventureType validType = adventureTypeRepository.findById(adventureType.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Adventure type not found with id: " + adventureType.getId()));
            validatedAdventureTypes.add(validType);
        }
        hiddenGem.setAdventureTypes(validatedAdventureTypes);

        return hiddenGemRepository.save(hiddenGem);
    }

    /**
     * Update an existing hidden gem
     */
    public HiddenGem updateHiddenGem(Long id, HiddenGem hiddenGemDetails) {
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
        if (hiddenGemDetails.getState() != null && hiddenGemDetails.getState().getId() != null) {
            State state = stateRepository.findById(hiddenGemDetails.getState().getId())
                    .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + hiddenGemDetails.getState().getId()));
            hiddenGem.setState(state);
        }

        // Update adventure types if provided
        if (hiddenGemDetails.getAdventureTypes() != null && !hiddenGemDetails.getAdventureTypes().isEmpty()) {
            Set<AdventureType> validatedAdventureTypes = new HashSet<>();
            for (AdventureType adventureType : hiddenGemDetails.getAdventureTypes()) {
                AdventureType validType = adventureTypeRepository.findById(adventureType.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Adventure type not found with id: " + adventureType.getId()));
                validatedAdventureTypes.add(validType);
            }
            hiddenGem.setAdventureTypes(validatedAdventureTypes);
        }

        return hiddenGemRepository.save(hiddenGem);
    }

    /**
     * Delete a hidden gem
     */
    public void deleteHiddenGem(Long id) {
        HiddenGem hiddenGem = hiddenGemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hidden gem not found with id: " + id));
        hiddenGemRepository.delete(hiddenGem);
    }

    /**
     * Get hidden gems by state
     */
    public Page<HiddenGem> getHiddenGemsByState(Long stateId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return hiddenGemRepository.findByStateId(stateId, pageable);
    }

    /**
     * Get hidden gems by adventure types
     */
    public Page<HiddenGem> getHiddenGemsByAdventureTypes(List<Long> adventureTypeIds, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return hiddenGemRepository.findByAdventureTypeIds(adventureTypeIds, pageable);
    }

    /**
     * Get statistics
     */
    public long getTotalHiddenGemsCount() {
        return hiddenGemRepository.count();
    }

    public long getHiddenGemsCountByState(Long stateId) {
        return hiddenGemRepository.countByStateId(stateId);
    }

    public long getHiddenGemsCountByAdventureType(Long adventureTypeId) {
        return hiddenGemRepository.countByAdventureTypeId(adventureTypeId);
    }

    /**
     * Validate sort field
     */
    public String validateSortField(String sortBy) {
        // Define allowed sort fields
        Set<String> allowedSortFields = Set.of(
            "id", "name", "createdAt", "updatedAt", "state.name", 
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
