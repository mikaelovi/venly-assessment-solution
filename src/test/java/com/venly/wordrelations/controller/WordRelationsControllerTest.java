package com.venly.wordrelations.controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .andExpect(status()
                        .isCreated()).andReturn();

        final var resultContent = mapper.readValue(result.getResponse().getContentAsString(), WordRelation.class);

        assertNotNull(resultContent);
        assertEquals(request.getWord(), resultContent.getWord());
        assertEquals(request.getAnotherWord(), resultContent.getAnotherWord());
        assertEquals(request.getRelation(), resultContent.getRelation());
    }
}