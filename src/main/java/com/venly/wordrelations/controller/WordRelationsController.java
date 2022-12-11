package com.venly.wordrelations.controller;

import com.venly.wordrelations.controller.request.CreateRelation;
import com.venly.wordrelations.entity.WordRelation;
import com.venly.wordrelations.enumeration.RelationType;
import com.venly.wordrelations.service.WordRelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

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

    @GetMapping("all")
    public ResponseEntity<List<WordRelation>> getAll() {

        final var allWordRels = wordRelationService.findAll();

        if (allWordRels.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(allWordRels);
    }

    @GetMapping("filter-by-relation/{relation}")
    public ResponseEntity<WordRelation> filterByRelation(@PathVariable final String relation) {
        final var relationType = RelationType.getMatching(relation);

        if (Objects.isNull(relationType)) {
            return ResponseEntity.badRequest().build();
        }

        final var allWordRels = wordRelationService.findAll();

        if (allWordRels.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        final var filtered = allWordRels.stream().filter(wordRelation -> wordRelation.getRelation() == relationType).findFirst();

        if (filtered.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(filtered.get());
    }
}
