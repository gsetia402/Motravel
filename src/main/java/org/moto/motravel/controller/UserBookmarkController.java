package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.moto.motravel.model.HiddenGemBookmark;
import org.moto.motravel.service.HiddenGemBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users/bookmarks")
@Tag(name = "User Bookmarks", description = "User bookmark management APIs")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class UserBookmarkController {

    @Autowired
    private HiddenGemBookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "Get user's bookmarked hidden gems")
    public ResponseEntity<?> getUserBookmarks(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "bookmarkedAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            if (page == -1) {
                // Return all bookmarks without pagination
                List<HiddenGemBookmark> bookmarks = bookmarkService.getUserBookmarks(userId);
                return ResponseEntity.ok(Map.of(
                    "bookmarks", bookmarks,
                    "totalCount", bookmarks.size()
                ));
            } else {
                // Return paginated bookmarks
                Page<HiddenGemBookmark> bookmarks = bookmarkService.getUserBookmarks(userId, page, size, sortBy, sortDirection);
                return ResponseEntity.ok(bookmarks);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch bookmarks"));
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get user's bookmark count")
    public ResponseEntity<?> getUserBookmarkCount() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            long count = bookmarkService.getUserBookmarkCount(userId);
            return ResponseEntity.ok(Map.of("bookmarkCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch bookmark count"));
        }
    }

    @GetMapping("/check/{hiddenGemId}")
    @Operation(summary = "Check if user has bookmarked a specific hidden gem")
    public ResponseEntity<?> checkBookmarkStatus(@PathVariable Long hiddenGemId) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());

            boolean isBookmarked = bookmarkService.isBookmarked(userId, hiddenGemId);
            return ResponseEntity.ok(Map.of(
                "isBookmarked", isBookmarked,
                "hiddenGemId", hiddenGemId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to check bookmark status"));
        }
    }
}
