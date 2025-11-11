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
}
