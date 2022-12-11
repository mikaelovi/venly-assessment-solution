package com.venly.wordrelations.service;

import com.venly.wordrelations.entity.WordRelation;

import java.util.List;


public interface WordRelationService {
    WordRelation create(WordRelation wordRelation);

    List<WordRelation> findAll();
}
