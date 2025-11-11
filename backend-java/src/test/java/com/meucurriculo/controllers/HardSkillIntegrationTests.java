package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.HardSkillInputDTO;
import com.meucurriculo.repositories.HardSkillRepository;
import com.meucurriculo.testsupport.BaseHttpIntegrationTests;
import com.meucurriculo.testsupport.HttpJsonClient;
import com.meucurriculo.testsupport.clients.HardSkillClient;
import com.meucurriculo.testsupport.factories.HardSkillFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.*;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HardSkillIntegrationTests extends BaseHttpIntegrationTests {

    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired private HardSkillRepository hardSkillRepository;

    private HardSkillClient client() {
        return new HardSkillClient(new HttpJsonClient(), baseUrl(""));
    }

    @BeforeEach
    void cleanHardSkills() {
        hardSkillRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createHardSkill_shouldPersistAndReturnDTO() {
        HardSkillInputDTO java = HardSkillFactory.javaSkill();
        ResponseEntity<@NotNull String> response = client().create(java, jsonHeaders());

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull().contains("Java");
    }

    @Test
    @Order(2)
    void createHardSkill_shouldReturn422_whenNameIsBlank() {
        HardSkillInputDTO blank = HardSkillFactory.blankNameSkill();

        assertThatThrownBy(() -> client().create(blank, jsonHeaders()))
                .isInstanceOf(org.springframework.web.client.HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    var e = (org.springframework.web.client.HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    String body = e.getResponseBodyAsString();
            assertThat(body).contains(
                "\"status\":422",
                "\"path\":\"/hardskills\"",
                "\"error\":\"Erro de validação\"",
                "\"customFieldErrors\"",
                "\"name\":\"name\""
            );
                });
    }

    @Test
    @Order(3)
    void listHardSkills_shouldReturnAll() throws Exception {
        client().create(HardSkillFactory.javaSkill(), jsonHeaders());
        client().create(HardSkillFactory.springSkill(), jsonHeaders());

        ResponseEntity<String> response = rest.getForEntity(baseUrl("/hardskills"), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Map<String, Object>> list = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(list).isNotEmpty();
        assertThat(response.getBody()).contains("Java", "Spring");
    }

    @Test
    @Order(4)
    void updateHardSkill_shouldPersistChanges() throws Exception {
        // arrange create
        var created = client().create(HardSkillFactory.javaSkill(), jsonHeaders());
        Map<String, Object> dto = objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {});
        long id = ((Number) dto.get("id")).longValue();

        var update = new HardSkillInputDTO("Updated Skill");
        var entity = new org.springframework.http.HttpEntity<>(update, jsonHeaders());
        ResponseEntity<String> response = rest.exchange(baseUrl("/hardskills/" + id), HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Updated Skill");
    }

    @Test
    @Order(5)
    void updateHardSkill_shouldReturn404_whenNotExists() {
        var update = new HardSkillInputDTO("Anything");
        var entity = new org.springframework.http.HttpEntity<>(update, jsonHeaders());
        long unknownId = 999999L;
        ResponseEntity<String> response = rest.exchange(baseUrl("/hardskills/" + unknownId), HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }

    @Test
    @Order(6)
    void updateHardSkill_shouldReturn422_whenNameBlank() throws Exception {
        var created = client().create(HardSkillFactory.springSkill(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();
        var invalid = HardSkillFactory.blankNameSkill();
        var entity = new org.springframework.http.HttpEntity<>(invalid, jsonHeaders());
        ResponseEntity<String> response = rest.exchange(baseUrl("/hardskills/" + id), HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(422);
        assertThat(response.getBody()).contains("\"error\":\"Erro de validação\"", "\"name\":\"name\"");
    }

    @Test
    @Order(7)
    void deleteHardSkill_shouldReturn204_andRemoveEntity() throws Exception {
        var created = client().create(HardSkillFactory.springSkill(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();
        ResponseEntity<Void> response = rest.exchange(baseUrl("/hardskills/" + id), HttpMethod.DELETE, new org.springframework.http.HttpEntity<>(jsonHeaders()), Void.class);
        assertThat(response.getStatusCode().value()).isEqualTo(204);

        ResponseEntity<String> after = rest.getForEntity(baseUrl("/hardskills/" + id), String.class);
        assertThat(after.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @Order(8)
    void deleteHardSkill_shouldReturn404_whenNotExists() {
        long unknownId = 123456L;
        ResponseEntity<String> response = rest.exchange(baseUrl("/hardskills/" + unknownId), HttpMethod.DELETE, new org.springframework.http.HttpEntity<>(jsonHeaders()), String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }
}
