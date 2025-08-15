package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.moto.motravel.model.HiddenGem;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.service.HiddenGemBookmarkService;
import org.moto.motravel.service.HiddenGemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hidden-gems")
@Tag(name = "Hidden Gems", description = "Hidden travel destinations management APIs")
public class HiddenGemController {

    @Autowired
    private HiddenGemService hiddenGemService;

    @Autowired
    private HiddenGemBookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "Get all hidden gems with optional filtering and pagination")
    public ResponseEntity<Page<HiddenGem>> getAllHiddenGems(
            @Parameter(description = "State ID for filtering") @RequestParam(required = false) Long stateId,
            @Parameter(description = "Adventure type IDs for filtering") @RequestParam(required = false) List<Long> adventureTypeIds,
            @Parameter(description = "Search term for name/description") @RequestParam(required = false) String search,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection) {

        // Validate and sanitize parameters
        sortBy = hiddenGemService.validateSortField(sortBy);
        sortDirection = hiddenGemService.validateSortDirection(sortDirection);

        Page<HiddenGem> hiddenGems = hiddenGemService.searchHiddenGems(
                stateId, adventureTypeIds, search, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(hiddenGems);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hidden gem by ID")
    public ResponseEntity<?> getHiddenGemById(@PathVariable Long id) {
        return hiddenGemService.getHiddenGemById(id)
                .map(hiddenGem -> {
                    // Add bookmark status if user is authenticated
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                        try {
                            UserDetails userDetails = (UserDetails) auth.getPrincipal();
                            Long userId = Long.parseLong(userDetails.getUsername());
                            boolean isBookmarked = bookmarkService.isBookmarked(userId, id);
                            
                            return ResponseEntity.ok(Map.of(
                                "hiddenGem", hiddenGem,
                                "isBookmarked", isBookmarked,
                                "bookmarkCount", bookmarkService.getHiddenGemBookmarkCount(id)
                            ));
                        } catch (Exception e) {
                            // If user parsing fails, just return the gem without bookmark info
                            return ResponseEntity.ok(hiddenGem);
                        }
                    }
                    return ResponseEntity.ok(hiddenGem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find hidden gems near a location")
    public ResponseEntity<List<HiddenGem>> findNearbyGems(
            @Parameter(description = "Latitude") @RequestParam Double latitude,
            @Parameter(description = "Longitude") @RequestParam Double longitude,
            @Parameter(description = "Search radius in kilometers") @RequestParam(defaultValue = "50.0") Double radius) {

        List<HiddenGem> nearbyGems = hiddenGemService.findNearbyGems(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyGems);
    }

    @PostMapping("/{id}/bookmark")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Bookmark a hidden gem", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> bookmarkHiddenGem(@PathVariable Long id) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            bookmarkService.addBookmark(userId, id);
            return ResponseEntity.ok(new MessageResponse("Hidden gem bookmarked successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to bookmark hidden gem"));
        }
    }

    @DeleteMapping("/{id}/bookmark")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Remove bookmark from a hidden gem", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> removeBookmark(@PathVariable Long id) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            bookmarkService.removeBookmark(userId, id);
            return ResponseEntity.ok(new MessageResponse("Bookmark removed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to remove bookmark"));
        }
    }

    @PostMapping("/{id}/toggle-bookmark")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Toggle bookmark status for a hidden gem", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> toggleBookmark(@PathVariable Long id) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            boolean isBookmarked = bookmarkService.toggleBookmark(userId, id);
            String message = isBookmarked ? "Hidden gem bookmarked successfully" : "Bookmark removed successfully";
            
            return ResponseEntity.ok(Map.of(
                "message", message,
                "isBookmarked", isBookmarked,
                "bookmarkCount", bookmarkService.getHiddenGemBookmarkCount(id)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to toggle bookmark"));
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Get hidden gems statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        long totalCount = hiddenGemService.getTotalHiddenGemsCount();
        
        return ResponseEntity.ok(Map.of(
            "totalHiddenGems", totalCount
        ));
    }
}
