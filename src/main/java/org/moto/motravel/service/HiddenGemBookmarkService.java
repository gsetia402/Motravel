package org.moto.motravel.service;

import org.moto.motravel.model.HiddenGem;
import org.moto.motravel.model.HiddenGemBookmark;
import org.moto.motravel.repository.HiddenGemBookmarkRepository;
import org.moto.motravel.repository.HiddenGemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiddenGemBookmarkService {

    @Autowired
    private HiddenGemBookmarkRepository bookmarkRepository;

    @Autowired
    private HiddenGemRepository hiddenGemRepository;

    /**
     * Add a bookmark for a user
     */
    public HiddenGemBookmark addBookmark(String userId, String hiddenGemId) {
        // Check if hidden gem exists
        HiddenGem hiddenGem = hiddenGemRepository.findById(hiddenGemId)
                .orElseThrow(() -> new IllegalArgumentException("Hidden gem not found with id: " + hiddenGemId));

        // Check if bookmark already exists
        if (bookmarkRepository.existsByUserIdAndHiddenGemId(userId, hiddenGemId)) {
            throw new IllegalStateException("User has already bookmarked this hidden gem");
        }

        HiddenGemBookmark bookmark = new HiddenGemBookmark(userId, hiddenGemId);
        return bookmarkRepository.save(bookmark);
    }

    /**
     * Remove a bookmark for a user
     */
    public void removeBookmark(String userId, String hiddenGemId) {
        var existing = bookmarkRepository.findByUserId(userId).stream()
                .filter(b -> hiddenGemId.equals(b.getHiddenGemId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found for user " + userId + " and hidden gem " + hiddenGemId));
        bookmarkRepository.deleteById(existing.getId());
    }

    /**
     * Check if user has bookmarked a specific hidden gem
     */
    public boolean isBookmarked(String userId, String hiddenGemId) {
        return bookmarkRepository.existsByUserIdAndHiddenGemId(userId, hiddenGemId);
    }

    /**
     * Get all bookmarks for a user
     */
    public List<HiddenGemBookmark> getUserBookmarks(String userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    /**
     * Get user bookmarks with pagination
     */
    public Page<HiddenGemBookmark> getUserBookmarks(String userId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), validateSortField(sortBy));
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookmarkRepository.findByUserId(userId, pageable);
    }

    /**
     * Get count of bookmarks for a user
     */
    public long getUserBookmarkCount(String userId) {
        return bookmarkRepository.countByUserId(userId);
    }

    /**
     * Get count of bookmarks for a hidden gem (popularity indicator)
     */
    public long getHiddenGemBookmarkCount(String hiddenGemId) {
        return bookmarkRepository.countByHiddenGemId(hiddenGemId);
    }

    /**
     * Toggle bookmark status (add if not exists, remove if exists)
     */
    public boolean toggleBookmark(String userId, String hiddenGemId) {
        if (isBookmarked(userId, hiddenGemId)) {
            removeBookmark(userId, hiddenGemId);
            return false; // Bookmark removed
        } else {
            addBookmark(userId, hiddenGemId);
            return true; // Bookmark added
        }
    }

    /**
     * Validate sort field for bookmarks
     */
    private String validateSortField(String sortBy) {
        // Define allowed sort fields for bookmarks
        if (sortBy == null || (!sortBy.equals("bookmarkedAt"))) {
            return "bookmarkedAt"; // Default sort field
        }
        return sortBy;
    }
}
