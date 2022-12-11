package com.venly.wordrelations.service;

import com.venly.wordrelations.WordRelationRepository;
import com.venly.wordrelations.entity.WordRelation;
import com.venly.wordrelations.enumeration.RelationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class WordRelationServiceTest {

    @MockBean
    private WordRelationRepository wordRelationRepository;

    private WordRelationService wordRelationService;

    @BeforeEach
    void setup() {
        wordRelationService = new WordRelationServiceImpl(wordRelationRepository);
    }

    @Test
    void testCreateWordRelation() {
        final var newWordRel = new WordRelation("son", "daughter", RelationType.RELATED);

        when(wordRelationRepository.save(any(WordRelation.class))).thenReturn(newWordRel);
        final var actual = wordRelationService.create(newWordRel);

        assertNotNull(actual);
        assertEquals(newWordRel, actual);
    }



    @Test
    void testListEntries() {
        final WordRelation[] wordRelations = new WordRelation[3];

        wordRelations[0] = new WordRelation("son", "daughter", RelationType.ANTONYM);
        wordRelations[1] = new WordRelation("road", "street", RelationType.SYNONYM);
        wordRelations[2] = new WordRelation("road", "avenue", RelationType.RELATED);

        when(wordRelationRepository.findAll()).thenReturn(Arrays.asList(wordRelations));

        final var actual = wordRelationService.findAll();

        assertEquals(wordRelations.length, actual.size());
    }
}