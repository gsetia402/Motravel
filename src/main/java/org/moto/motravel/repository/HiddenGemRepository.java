package org.moto.motravel.repository;

import org.moto.motravel.model.HiddenGem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiddenGemRepository extends MongoRepository<HiddenGem, String> {
    long countByAdventureTypeIdsContains(String adventureTypeId);
    long countByStateId(String stateId);
    Page<HiddenGem> findByStateId(String stateId, Pageable pageable);
    Page<HiddenGem> findByAdventureTypeIdsIn(java.util.List<String> adventureTypeIds, Pageable pageable);
}
