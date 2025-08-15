package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.AdventureType;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.AdventureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/adventure-types")
@Tag(name = "Admin - Adventure Types", description = "Admin APIs for managing adventure types")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminAdventureTypeController {

    @Autowired
    private AdventureTypeService adventureTypeService;

    @PostMapping
    @Operation(summary = "Create a new adventure type (Admin only)")
    public ResponseEntity<?> createAdventureType(@Valid @RequestBody AdventureType adventureType) {
        try {
            AdventureType createdType = adventureTypeService.createAdventureType(adventureType);
            return new ResponseEntity<>(createdType, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to create adventure type: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an adventure type (Admin only)")
    public ResponseEntity<?> updateAdventureType(@PathVariable Long id, @Valid @RequestBody AdventureType adventureTypeDetails) {
        try {
            AdventureType updatedType = adventureTypeService.updateAdventureType(id, adventureTypeDetails);
            return ResponseEntity.ok(updatedType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to update adventure type: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an adventure type (Admin only)")
    public ResponseEntity<?> deleteAdventureType(@PathVariable Long id) {
        try {
            adventureTypeService.deleteAdventureType(id);
            return ResponseEntity.ok(new MessageResponse("Adventure type deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete adventure type: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all adventure types for admin management")
    public ResponseEntity<List<AdventureType>> getAllAdventureTypesForAdmin() {
        List<AdventureType> adventureTypes = adventureTypeService.getAllAdventureTypes();
        return ResponseEntity.ok(adventureTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get adventure type by ID (Admin view)")
    public ResponseEntity<?> getAdventureTypeById(@PathVariable Long id) {
        return adventureTypeService.getAdventureTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
