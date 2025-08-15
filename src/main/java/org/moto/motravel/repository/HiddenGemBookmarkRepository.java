package org.moto.motravel.repository;

import org.moto.motravel.model.HiddenGemBookmark;
import org.moto.motravel.model.HiddenGemBookmarkId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiddenGemBookmarkRepository extends JpaRepository<HiddenGemBookmark, HiddenGemBookmarkId> {
    
    // Find all bookmarks for a user
    List<HiddenGemBookmark> findByUserId(Long userId);
    
    // Find all bookmarks for a user with pagination
    Page<HiddenGemBookmark> findByUserId(Long userId, Pageable pageable);
    
    // Check if user has bookmarked a specific gem
    boolean existsByUserIdAndHiddenGemId(Long userId, Long hiddenGemId);
    
    // Count bookmarks for a user
    long countByUserId(Long userId);
    
    // Count bookmarks for a hidden gem
    long countByHiddenGemId(Long hiddenGemId);
    
    // Get user's bookmarked gems with full gem details
    @Query("SELECT b FROM HiddenGemBookmark b JOIN FETCH b.hiddenGem h JOIN FETCH h.state JOIN FETCH h.adventureTypes WHERE b.userId = :userId ORDER BY b.bookmarkedAt DESC")
    List<HiddenGemBookmark> findByUserIdWithGemDetails(@Param("userId") Long userId);
    
    // Get user's bookmarked gems with pagination and full details
    @Query("SELECT b FROM HiddenGemBookmark b JOIN FETCH b.hiddenGem h JOIN FETCH h.state JOIN FETCH h.adventureTypes WHERE b.userId = :userId")
    Page<HiddenGemBookmark> findByUserIdWithGemDetails(@Param("userId") Long userId, Pageable pageable);
}
