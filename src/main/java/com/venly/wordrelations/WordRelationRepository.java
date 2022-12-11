package com.venly.wordrelations;

import com.venly.wordrelations.entity.WordRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRelationRepository extends JpaRepository<WordRelation, Long> {
}
