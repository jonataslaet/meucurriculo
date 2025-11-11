package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;
import com.meucurriculo.repositories.ProjectRepository;
import com.meucurriculo.testsupport.BaseHttpIntegrationTests;
import com.meucurriculo.testsupport.HttpJsonClient;
import com.meucurriculo.testsupport.clients.ProjectClient;
import com.meucurriculo.testsupport.factories.ProjectFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectIntegrationTests extends BaseHttpIntegrationTests {

    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired private ProjectRepository projectRepository;

    private ProjectClient client() { return new ProjectClient(new HttpJsonClient(), baseUrl("")); }

    @BeforeEach
    void clean() { projectRepository.deleteAll(); }

    @Test
    void createProject_shouldPersistAndReturnDTO() {
        ProjectInputDTO dto = ProjectFactory.sampleOngoingProject();
        ResponseEntity<@NotNull String> response = client().create(dto, jsonHeaders());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull().contains("Build portfolio site");
    }

    @Test
    void createProject_shouldReturn422_whenDescriptionBlank() {
        ProjectInputDTO dto = ProjectFactory.blankDescription();
        assertThatThrownBy(() -> client().create(dto, jsonHeaders()))
                .isInstanceOf(org.springframework.web.client.HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    var e = (org.springframework.web.client.HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    assertThat(e.getResponseBodyAsString()).contains("\"name\":\"description\"");
                });
    }

    @Test
    void listProjects_shouldReturnAll() throws Exception {
        client().create(ProjectFactory.sampleOngoingProject(), jsonHeaders());
        client().create(ProjectFactory.sampleFinishedProject(), jsonHeaders());

        ResponseEntity<String> response = rest.getForEntity(baseUrl("/projects"), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Map<String, Object>> list = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(list).isNotEmpty();
        assertThat(response.getBody()).contains("Build portfolio site", "Contribute to OSS");
    }

    @Test
    void updateProject_shouldPersistChanges() throws Exception {
        var created = client().create(ProjectFactory.sampleOngoingProject(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();

        var update = new ProjectInputDTO("Updated Desc", java.time.LocalDate.of(2024, 2, 1), null);
        var entity = new org.springframework.http.HttpEntity<>(update, jsonHeaders());
        var response = new TestRestTemplate().exchange(baseUrl("/projects/" + id), org.springframework.http.HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Updated Desc", "2024-02-01");
    }

    @Test
    void updateProject_shouldReturn404_whenNotExists() {
        var update = new ProjectInputDTO("Any", java.time.LocalDate.of(2024, 1, 1), null);
        var entity = new org.springframework.http.HttpEntity<>(update, jsonHeaders());
        long unknownId = 987654L;
        var response = new TestRestTemplate().exchange(baseUrl("/projects/" + unknownId), org.springframework.http.HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }

    @Test
    void updateProject_shouldReturn422_whenDescriptionBlank() throws Exception {
        var created = client().create(ProjectFactory.sampleOngoingProject(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();
        var invalid = ProjectFactory.blankDescription();
        var entity = new org.springframework.http.HttpEntity<>(invalid, jsonHeaders());
        var response = new TestRestTemplate().exchange(baseUrl("/projects/" + id), org.springframework.http.HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(422);
        assertThat(response.getBody()).contains("\"error\":\"Erro de validação\"", "\"name\":\"description\"");
    }

    @Test
    void deleteProject_shouldReturn204_andRemoveEntity() throws Exception {
        var created = client().create(ProjectFactory.sampleOngoingProject(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();
        var response = new TestRestTemplate().exchange(baseUrl("/projects/" + id), org.springframework.http.HttpMethod.DELETE, new org.springframework.http.HttpEntity<>(jsonHeaders()), Void.class);
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        var after = new TestRestTemplate().getForEntity(baseUrl("/projects/" + id), String.class);
        assertThat(after.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deleteProject_shouldReturn404_whenNotExists() {
        long unknownId = 11223344L;
        var response = new TestRestTemplate().exchange(baseUrl("/projects/" + unknownId), org.springframework.http.HttpMethod.DELETE, new org.springframework.http.HttpEntity<>(jsonHeaders()), String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }
}
