package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.repositories.ProjectRepository;
import com.meucurriculo.repositories.HardSkillProjectRepository;
import com.meucurriculo.testsupport.BaseHttpIntegrationTests;
import com.meucurriculo.testsupport.HttpJsonClient;
import com.meucurriculo.testsupport.clients.HardSkillProjectClient;
import com.meucurriculo.testsupport.factories.HardSkillProjectFactory;
import com.meucurriculo.testsupport.factories.ProjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import tools.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HardSkillProjectIntegrationTests extends BaseHttpIntegrationTests {

    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired private ProjectRepository projectRepository;
    @Autowired private HardSkillProjectRepository hardSkillProjectRepository;

    private HardSkillProjectClient client() { return new HardSkillProjectClient(new HttpJsonClient(), baseUrl("")); }

    private Long createProject() {
        var project = ProjectFactory.sampleOngoingProject();
        var response = rest.postForEntity(baseUrl("/projects"), new HttpEntity<>(project, jsonHeaders()), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<String, Object> dto = readJson(response.getBody());
        return ((Number) dto.get("id")).longValue();
    }

    private Long createHardSkill() {
        var hs = new com.meucurriculo.controllers.dtos.HardSkillInputDTO("Java");
        var response = rest.postForEntity(baseUrl("/hardskills"), new HttpEntity<>(hs, jsonHeaders()), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        return ((Number) readJson(response.getBody()).get("id")).longValue();
    }

    @BeforeEach
    void clean() {
        hardSkillProjectRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void createHardSkillProject_shouldPersist() {
    Long projectId = createProject();
    Long hardSkillId = createHardSkill();
    HardSkillProjectInputDTO dto = HardSkillProjectFactory.sampleApplied(hardSkillId);
        ResponseEntity<String> response = client().create(projectId, dto, jsonHeaders());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Apply Java expertise");
    }

    @Test
    void createHardSkillProject_shouldReturn422_whenDescriptionBlank() {
    Long projectId = createProject();
    Long hardSkillId = createHardSkill();
    HardSkillProjectInputDTO blank = HardSkillProjectFactory.blankDescription(hardSkillId);
        assertThatThrownBy(() -> client().create(projectId, blank, jsonHeaders()))
                .isInstanceOf(org.springframework.web.client.HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    var e = (org.springframework.web.client.HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    assertThat(e.getResponseBodyAsString()).contains("\"name\":\"description\"");
                });
    }

    @Test
    void createHardSkillProject_shouldReturn422_whenAppliedUntilBeforeAppliedSince() {
    Long projectId = createProject();
    HardSkillProjectInputDTO invalid = new HardSkillProjectInputDTO(
                "Invalid dates",
                LocalDate.of(2024, 3, 10),
        LocalDate.of(2024, 3, 1),
        createHardSkill()
        );
        assertThatThrownBy(() -> client().create(projectId, invalid, jsonHeaders()))
                .isInstanceOf(org.springframework.web.client.HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    var e = (org.springframework.web.client.HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    assertThat(e.getResponseBodyAsString()).contains("appliedUntil deve ser nulo ou maior/igual a appliedSince");
                });
    }

    @Test
    void listHardSkillProjects_shouldReturnAll() throws Exception {
    Long projectId = createProject();
    Long hardSkillId = createHardSkill();
    client().create(projectId, HardSkillProjectFactory.sampleApplied(hardSkillId), jsonHeaders());
    client().create(projectId, HardSkillProjectFactory.sampleFinished(hardSkillId), jsonHeaders());

        ResponseEntity<String> response = rest.getForEntity(baseUrl("/projects/" + projectId + "/hardskills"), String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Map<String, Object>> list = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(list).hasSizeGreaterThanOrEqualTo(2);
        assertThat(response.getBody()).contains("Apply Java expertise", "Refactoring sprint", "\"hardSkill\"", "\"name\":\"Java\"");
    }

    @Test
    void updateHardSkillProject_shouldPersistChanges() throws Exception {
    Long projectId = createProject();
    Long hardSkillId = createHardSkill();
    var created = client().create(projectId, HardSkillProjectFactory.sampleApplied(hardSkillId), jsonHeaders());
        Map<String,Object> createdDto = readJson(created.getBody());
        long pathHardSkillId = ((Number) createdDto.get("hardSkillId")).longValue();
        String since = (String) createdDto.get("appliedSince");
        // Change appliedSince and appliedUntil
        HardSkillProjectInputDTO update = new HardSkillProjectInputDTO("Updated desc", LocalDate.of(2024,2,1), LocalDate.of(2024,3,1), pathHardSkillId);
        HttpEntity<HardSkillProjectInputDTO> entity = new HttpEntity<>(update, jsonHeaders());
        ResponseEntity<String> response = rest.exchange(baseUrl("/projects/" + projectId + "/hardskills/" + pathHardSkillId + "/" + since), HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Updated desc", "2024-02-01", "2024-03-01");
    }

    @Test
    void updateHardSkillProject_shouldReturn404_whenNotExists() {
    Long projectId = createProject();
    long pathHardSkillId = 999999L;
    HardSkillProjectInputDTO update = new HardSkillProjectInputDTO("Something", LocalDate.of(2024,1,1), null, pathHardSkillId);
        HttpEntity<HardSkillProjectInputDTO> entity = new HttpEntity<>(update, jsonHeaders());
        ResponseEntity<String> response = rest.exchange(baseUrl("/projects/" + projectId + "/hardskills/" + pathHardSkillId + "/2024-01-01"), HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }

    @Test
    void deleteHardSkillProject_shouldReturn204() throws Exception {
    Long projectId = createProject();
    Long hardSkillId = createHardSkill();
    var created = client().create(projectId, HardSkillProjectFactory.sampleApplied(hardSkillId), jsonHeaders());
        Map<String,Object> dto = readJson(created.getBody());
        long pathHardSkillId = ((Number) dto.get("hardSkillId")).longValue();
        String since = (String) dto.get("appliedSince");
        ResponseEntity<Void> response = rest.exchange(baseUrl("/projects/" + projectId + "/hardskills/" + pathHardSkillId + "/" + since), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        ResponseEntity<Void> after = rest.exchange(baseUrl("/projects/" + projectId + "/hardskills/" + pathHardSkillId + "/" + since), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
        assertThat(after.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deleteHardSkillProject_shouldReturn404_whenNotExists() {
        Long projectId = createProject();
        long pathHardSkillId = 123456L;
        ResponseEntity<String> response = rest.exchange(baseUrl("/projects/" + projectId + "/hardskills/" + pathHardSkillId + "/2024-01-01"), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }

    private Map<String, Object> readJson(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
