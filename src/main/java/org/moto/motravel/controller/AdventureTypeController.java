package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.moto.motravel.model.AdventureType;
import org.moto.motravel.service.AdventureTypeService;
import org.moto.motravel.service.HiddenGemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/adventure-types")
@Tag(name = "Adventure Types", description = "Adventure activity types management APIs")
public class AdventureTypeController {

    @Autowired
    private AdventureTypeService adventureTypeService;

    @Autowired
    private HiddenGemService hiddenGemService;

    @GetMapping
    @Operation(summary = "Get all adventure types")
    public ResponseEntity<List<AdventureType>> getAllAdventureTypes() {
        List<AdventureType> adventureTypes = adventureTypeService.getAllAdventureTypes();
        return ResponseEntity.ok(adventureTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get adventure type by ID")
    public ResponseEntity<?> getAdventureTypeById(@PathVariable Long id) {
        return adventureTypeService.getAdventureTypeById(id)
                .map(adventureType -> {
                    long hiddenGemsCount = hiddenGemService.getHiddenGemsCountByAdventureType(id);
                    return ResponseEntity.ok(Map.of(
                        "adventureType", adventureType,
                        "hiddenGemsCount", hiddenGemsCount
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search adventure types by name")
    public ResponseEntity<List<AdventureType>> searchAdventureTypes(
            @Parameter(description = "Search term") @RequestParam String q) {
        List<AdventureType> adventureTypes = adventureTypeService.searchAdventureTypesByName(q);
        return ResponseEntity.ok(adventureTypes);
    }

    @GetMapping("/{id}/hidden-gems")
    @Operation(summary = "Get hidden gems for a specific adventure type")
    public ResponseEntity<?> getHiddenGemsByAdventureType(
            @PathVariable Long id,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection) {

        // Validate adventure type exists
        if (!adventureTypeService.getAdventureTypeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Validate and sanitize parameters
        sortBy = hiddenGemService.validateSortField(sortBy);
        sortDirection = hiddenGemService.validateSortDirection(sortDirection);

        var hiddenGems = hiddenGemService.getHiddenGemsByAdventureTypes(List.of(id), page, size, sortBy, sortDirection);
        return ResponseEntity.ok(hiddenGems);
    }
}
