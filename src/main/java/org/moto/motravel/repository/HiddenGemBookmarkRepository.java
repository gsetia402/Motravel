package org.moto.motravel.repository;

import org.moto.motravel.model.HiddenGemBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiddenGemBookmarkRepository extends MongoRepository<HiddenGemBookmark, String> {
    List<HiddenGemBookmark> findByUserId(String userId);
    Page<HiddenGemBookmark> findByUserId(String userId, Pageable pageable);
    boolean existsByUserIdAndHiddenGemId(String userId, String hiddenGemId);
    long countByUserId(String userId);
    long countByHiddenGemId(String hiddenGemId);
}
