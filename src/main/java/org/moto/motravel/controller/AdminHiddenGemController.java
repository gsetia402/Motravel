package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.HiddenGem;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.HiddenGemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/hidden-gems")
@Tag(name = "Admin - Hidden Gems", description = "Admin APIs for managing hidden gems")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminHiddenGemController {

    @Autowired
    private HiddenGemService hiddenGemService;

    @PostMapping
    @Operation(summary = "Create a new hidden gem (Admin only)")
    public ResponseEntity<?> createHiddenGem(@Valid @RequestBody HiddenGem hiddenGem) {
        try {
            HiddenGem createdGem = hiddenGemService.createHiddenGem(hiddenGem);
            return new ResponseEntity<>(createdGem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to create hidden gem: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a hidden gem (Admin only)")
    public ResponseEntity<?> updateHiddenGem(@PathVariable Long id, @Valid @RequestBody HiddenGem hiddenGemDetails) {
        try {
            HiddenGem updatedGem = hiddenGemService.updateHiddenGem(id, hiddenGemDetails);
            return ResponseEntity.ok(updatedGem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to update hidden gem: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a hidden gem (Admin only)")
    public ResponseEntity<?> deleteHiddenGem(@PathVariable Long id) {
        try {
            hiddenGemService.deleteHiddenGem(id);
            return ResponseEntity.ok(new MessageResponse("Hidden gem deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete hidden gem: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hidden gem by ID (Admin view with all details)")
    public ResponseEntity<?> getHiddenGemById(@PathVariable Long id) {
        return hiddenGemService.getHiddenGemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all hidden gems for admin management")
    public ResponseEntity<?> getAllHiddenGemsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        // Validate and sanitize parameters
        sortBy = hiddenGemService.validateSortField(sortBy);
        sortDirection = hiddenGemService.validateSortDirection(sortDirection);

        var hiddenGems = hiddenGemService.getAllHiddenGems(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(hiddenGems);
    }
}
