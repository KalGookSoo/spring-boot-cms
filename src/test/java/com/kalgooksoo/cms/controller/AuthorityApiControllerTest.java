package com.kalgooksoo.cms.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthorityApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String basePath = "/api/authorities";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveAllShouldReturnOk() throws Exception {
        // Given
        ArrayNode commands = objectMapper.createArrayNode();
        commands.add(createNode(objectMapper, "ROLE_MANAGER", "관리자"));
        commands.add(createNode(objectMapper, "ROLE_STUDENT", "학생"));

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post(basePath)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commands.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static JsonNode createNode(ObjectMapper objectMapper, String name, String alias) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", name);
        objectNode.put("alias", alias);
        return objectNode;
    }

}