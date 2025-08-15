package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.State;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/states")
@Tag(name = "Admin - States", description = "Admin APIs for managing states")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminStateController {

    @Autowired
    private StateService stateService;

    @PostMapping
    @Operation(summary = "Create a new state (Admin only)")
    public ResponseEntity<?> createState(@Valid @RequestBody State state) {
        try {
            State createdState = stateService.createState(state);
            return new ResponseEntity<>(createdState, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to create state: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a state (Admin only)")
    public ResponseEntity<?> updateState(@PathVariable Long id, @Valid @RequestBody State stateDetails) {
        try {
            State updatedState = stateService.updateState(id, stateDetails);
            return ResponseEntity.ok(updatedState);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to update state: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a state (Admin only)")
    public ResponseEntity<?> deleteState(@PathVariable Long id) {
        try {
            stateService.deleteState(id);
            return ResponseEntity.ok(new MessageResponse("State deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete state: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all states for admin management")
    public ResponseEntity<List<State>> getAllStatesForAdmin() {
        List<State> states = stateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get state by ID (Admin view)")
    public ResponseEntity<?> getStateById(@PathVariable Long id) {
        return stateService.getStateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
