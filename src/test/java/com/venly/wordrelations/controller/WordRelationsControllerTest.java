package com.venly.wordrelations.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.venly.wordrelations.entity.WordRelation;
import com.venly.wordrelations.enumeration.RelationType;
import com.venly.wordrelations.service.WordRelationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordRelationsController.class)
class WordRelationsControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @MockBean
    private WordRelationService wordRelationService;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
    }


    @Test
    void testCreateWordRelation() throws Exception {
        final var request = new WordRelation("son", "daughter", RelationType.ANTONYM);

        when(wordRelationService.create(any(WordRelation.class))).thenReturn(request);

        final var result = mockMvc.perform(MockMvcRequestBuilders.post("/word-rel/create")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        final var resultContent = mapper.readValue(result.getResponse().getContentAsString(), WordRelation.class);

        assertNotNull(resultContent);
        assertEquals(request.getWord(), resultContent.getWord());
        assertEquals(request.getAnotherWord(), resultContent.getAnotherWord());
        assertEquals(request.getRelation(), resultContent.getRelation());
    }


    @Test
    void testListEntries() throws Exception {
        final WordRelation[] wordRelations = new WordRelation[3];

        wordRelations[0] = new WordRelation("son", "daughter", RelationType.ANTONYM);
        wordRelations[1] = new WordRelation("road", "street", RelationType.SYNONYM);
        wordRelations[2] = new WordRelation("road", "avenue", RelationType.RELATED);

        when(wordRelationService.findAll()).thenReturn(Arrays.asList(wordRelations));

        final var result = mockMvc.perform(MockMvcRequestBuilders.get("/word-rel/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final var resultContent = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<WordRelation>>() {
        });

        assertNotNull(resultContent);
        assertNotEquals(0, resultContent.size());
        assertEquals(resultContent.size(), wordRelations.length);
    }

    @Test
    void testFilterByRelationTypeWithNullRelation() throws Exception {
        final var relation = "somedummyrelation";

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/word-rel/filter-by-relation/%s", relation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testFilterByRelationTypeWithEmptyData() throws Exception {
        final var relation = "synonym";

        when(wordRelationService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/word-rel/filter-by-relation/%s", relation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testFilterByRelationTypeWithNoMatchingResult() throws Exception {
        final WordRelation[] wordRelations = new WordRelation[3];

        wordRelations[0] = new WordRelation("son", "daughter", RelationType.ANTONYM);
        wordRelations[1] = new WordRelation("road", "avenue", RelationType.RELATED);
        wordRelations[2] = new WordRelation("street", "house", RelationType.RELATED);

        final var relation = "synonym";

        when(wordRelationService.findAll()).thenReturn(Arrays.asList(wordRelations));

        final var result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/word-rel/filter-by-relation/%s", relation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testFilterByRelationTypeWithMatchingResult() throws Exception {
        final WordRelation[] wordRelations = new WordRelation[3];

        wordRelations[0] = new WordRelation("son", "daughter", RelationType.ANTONYM);
        wordRelations[2] = new WordRelation("road", "avenue", RelationType.RELATED);
        wordRelations[1] = new WordRelation("road", "street", RelationType.SYNONYM);

        final var relation = "synonym";

        when(wordRelationService.findAll()).thenReturn(Arrays.asList(wordRelations));

        final var result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/word-rel/filter-by-relation/%s", relation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final var resultContent = mapper.readValue(result.getResponse().getContentAsString(), WordRelation.class);

        assertNotNull(resultContent);
        assertEquals(wordRelations[1].getWord(), resultContent.getWord());
        assertEquals(wordRelations[1].getAnotherWord(), resultContent.getAnotherWord());
        assertEquals(wordRelations[1].getRelation(), resultContent.getRelation());
    }
}