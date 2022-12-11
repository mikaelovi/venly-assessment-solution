package com.venly.wordrelations.service;

import com.venly.wordrelations.WordRelationRepository;
import com.venly.wordrelations.entity.WordRelation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordRelationServiceImpl implements WordRelationService {
    private final WordRelationRepository wordRelationRepository;

    public WordRelationServiceImpl(WordRelationRepository wordRelationRepository) {
        this.wordRelationRepository = wordRelationRepository;
    }

    @Override
    public WordRelation create(WordRelation wordRelation) {
        return wordRelationRepository.save(wordRelation);
    }

    @Override
    public List<WordRelation> findAll() {
        return wordRelationRepository.findAll();
    }
}
