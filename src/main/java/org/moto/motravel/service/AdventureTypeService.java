package org.moto.motravel.service;

import org.moto.motravel.model.AdventureType;
import org.moto.motravel.repository.AdventureTypeRepository;
import org.moto.motravel.repository.HiddenGemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdventureTypeService {

    @Autowired
    private AdventureTypeRepository adventureTypeRepository;

    @Autowired
    private HiddenGemRepository hiddenGemRepository;

    /**
     * Get all adventure types ordered by name
     */
    public List<AdventureType> getAllAdventureTypes() {
        return adventureTypeRepository.findAllByOrderByNameAsc();
    }

    /**
     * Get adventure type by ID
     */
    public Optional<AdventureType> getAdventureTypeById(String id) {
        return adventureTypeRepository.findById(id);
    }

    /**
     * Get adventure type by name
     */
    public Optional<AdventureType> getAdventureTypeByName(String name) {
        return adventureTypeRepository.findByName(name);
    }

    /**
     * Create a new adventure type
     */
    public AdventureType createAdventureType(AdventureType adventureType) {
        if (adventureTypeRepository.existsByName(adventureType.getName())) {
            throw new IllegalArgumentException("Adventure type with name '" + adventureType.getName() + "' already exists");
        }
        return adventureTypeRepository.save(adventureType);
    }

    /**
     * Update an existing adventure type
     */
    public AdventureType updateAdventureType(String id, AdventureType adventureTypeDetails) {
        AdventureType adventureType = adventureTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adventure type not found with id: " + id));

        // Check if the new name conflicts with existing adventure types (excluding current one)
        if (!adventureType.getName().equals(adventureTypeDetails.getName()) && 
            adventureTypeRepository.existsByName(adventureTypeDetails.getName())) {
            throw new IllegalArgumentException("Adventure type with name '" + adventureTypeDetails.getName() + "' already exists");
        }

        adventureType.setName(adventureTypeDetails.getName());
        return adventureTypeRepository.save(adventureType);
    }

    /**
     * Delete an adventure type
     */
    public void deleteAdventureType(String id) {
        AdventureType adventureType = adventureTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adventure type not found with id: " + id));

        // Prevent deletion if any HiddenGem references this adventure type
        long refCount = hiddenGemRepository.countByAdventureTypeIdsContains(id);
        if (refCount > 0) {
            throw new IllegalStateException("Cannot delete adventure type referenced by hidden gems (" + refCount + ").");
        }

        adventureTypeRepository.delete(adventureType);
    }

    /**
     * Search adventure types by name
     */
    public List<AdventureType> searchAdventureTypesByName(String searchTerm) {
        return adventureTypeRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchTerm);
    }

    /**
     * Check if adventure type exists by name
     */
    public boolean existsByName(String name) {
        return adventureTypeRepository.existsByName(name);
    }
}
