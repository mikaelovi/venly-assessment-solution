package com.venly.wordrelations.controller;

import com.venly.wordrelations.controller.request.CreateRelation;
import com.venly.wordrelations.entity.WordRelation;
import com.venly.wordrelations.service.WordRelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("word-rel")
public class WordRelationsController {

    private final WordRelationService wordRelationService;

    public WordRelationsController(WordRelationService wordRelationService) {
        this.wordRelationService = wordRelationService;
    }

    @PostMapping("create")
    public ResponseEntity<WordRelation> create(@RequestBody CreateRelation createRelationRequest) {

        final var wordRel = new WordRelation(createRelationRequest.word(), createRelationRequest.anotherWord(), createRelationRequest.relation());

        final var savedWordRel = wordRelationService.create(wordRel);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedWordRel);
    }
}
