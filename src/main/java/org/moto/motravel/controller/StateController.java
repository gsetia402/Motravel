package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.moto.motravel.model.State;
import org.moto.motravel.service.HiddenGemService;
import org.moto.motravel.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/states")
@Tag(name = "States", description = "Indian states management APIs")
public class StateController {

    @Autowired
    private StateService stateService;

    @Autowired
    private HiddenGemService hiddenGemService;

    @GetMapping
    @Operation(summary = "Get all states")
    public ResponseEntity<List<State>> getAllStates() {
        List<State> states = stateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get state by ID")
    public ResponseEntity<?> getStateById(@PathVariable Long id) {
        return stateService.getStateById(id)
                .map(state -> {
                    long hiddenGemsCount = hiddenGemService.getHiddenGemsCountByState(id);
                    return ResponseEntity.ok(Map.of(
                        "state", state,
                        "hiddenGemsCount", hiddenGemsCount
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search states by name")
    public ResponseEntity<List<State>> searchStates(
            @Parameter(description = "Search term") @RequestParam String q) {
        List<State> states = stateService.searchStatesByName(q);
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{id}/hidden-gems")
    @Operation(summary = "Get hidden gems for a specific state")
    public ResponseEntity<?> getHiddenGemsByState(
            @PathVariable Long id,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection) {

        // Validate state exists
        if (!stateService.getStateById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Validate and sanitize parameters
        sortBy = hiddenGemService.validateSortField(sortBy);
        sortDirection = hiddenGemService.validateSortDirection(sortDirection);

        var hiddenGems = hiddenGemService.getHiddenGemsByState(id, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(hiddenGems);
    }
}
