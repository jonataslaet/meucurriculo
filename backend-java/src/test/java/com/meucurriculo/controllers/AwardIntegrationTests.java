package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.AwardInputDTO;
import com.meucurriculo.repositories.AwardRepository;
import com.meucurriculo.testsupport.BaseHttpIntegrationTests;
import com.meucurriculo.testsupport.HttpJsonClient;
import com.meucurriculo.testsupport.clients.AwardClient;
import com.meucurriculo.testsupport.factories.AwardFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AwardIntegrationTests extends BaseHttpIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @LocalServerPort private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired private ObjectMapper objectMapper;
    @Autowired private AwardRepository awardRepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/awards";
    }

    @BeforeEach
    void cleanDatabase() {
        awardRepository.deleteAll();
    }

    private AwardClient client() {
        return new AwardClient(new HttpJsonClient(), baseUrl(""));
    }

    @Test
    void createAward_shouldPersistAndReturnDTO() {
        AwardInputDTO bestDeveloper2025 = AwardFactory.awardBestDeveloper2025();
        ResponseEntity<@NotNull String> responseEntity = client().create(bestDeveloper2025, jsonHeaders());

        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).contains(bestDeveloper2025.title()).doesNotContain("Error");
        assertThat(responseEntity.getBody()).contains(bestDeveloper2025.description()).doesNotContain("Error");
        assertThat(responseEntity.getBody()).contains(String.valueOf(bestDeveloper2025.year())).doesNotContain("Error");

        assertThat(bestDeveloper2025.title()).isEqualTo("Best Developer");
        assertThat(bestDeveloper2025.description()).isEqualTo("Awarded for outstanding contributions");
        assertThat(bestDeveloper2025.year()).isEqualTo(2025);
    }

    @Test
    void createAward_shouldThrow422_andContainFieldError_whenTitleIsNull() {
        AwardInputDTO awardWithBlankTitle = AwardFactory.awardWithBlankTitle();

        assertThatThrownBy(() -> client().create(awardWithBlankTitle, jsonHeaders()))
                .isInstanceOf(HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    HttpClientErrorException e = (HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    String body = e.getResponseBodyAsString();
                    assertThat(body).contains("\"status\":422");
                    assertThat(body).contains("\"path\":\"/awards\"");
                    assertThat(body).contains("\"error\":\"Erro de validação\"");
                    assertThat(body).contains("\"customFieldErrors\"");
                    assertThat(body).contains("\"name\":\"title\"");
                    assertThat(body).contains("\"message\":\"não deve estar em branco\"");
                    assertThat(body).contains("Validation failed for argument [0]");
                });
    }

    @Test
    void createAward_shouldThrow422_andContainFieldError_whenDescriptionIsNull() {
        AwardInputDTO awardWithBlankDescription = AwardFactory.awardWithNullDescription();

        assertThatThrownBy(() -> client().create(awardWithBlankDescription, jsonHeaders()))
                .isInstanceOf(HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    HttpClientErrorException e = (HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    String body = e.getResponseBodyAsString();
                    assertThat(body).contains("\"status\":422",
                            "\"path\":\"/awards\"",
                            "\"error\":\"Erro de validação\"",
                            "\"customFieldErrors\"",
                            "\"name\":\"description\"");
                    assertThat(body).contains("não deve estar em branco");
                    assertThat(body).contains("Validation failed for argument [0]");
                });
    }

    @Test
    void createAward_shouldThrow422_andContainFieldError_whenDescriptionIsBlank() {
        AwardInputDTO awardWithBlankDescription = AwardFactory.awardWithBlankDescription();

        assertThatThrownBy(() -> client().create(awardWithBlankDescription, jsonHeaders()))
                .isInstanceOf(HttpClientErrorException.UnprocessableContent.class)
                .satisfies(ex -> {
                    HttpClientErrorException e = (HttpClientErrorException) ex;
                    assertThat(e.getStatusCode().value()).isEqualTo(422);
                    String body = e.getResponseBodyAsString();
                    assertThat(body).contains("\"status\":422",
                            "\"path\":\"/awards\"",
                            "\"error\":\"Erro de validação\"",
                            "\"customFieldErrors\"",
                            "\"name\":\"description\"",
                            "\"message\":\"não deve estar em branco\"");
                });
    }

    @Test
    void listAwards_shouldReturnAllPersistedAwards() {
        // arrange
        client().create(AwardFactory.awardBestDeveloper2025(), jsonHeaders());
        client().create(AwardFactory.awardCommunityStar2024(), jsonHeaders());

        // act
        ResponseEntity<@NotNull String> response = rest.getForEntity(baseUrl(), String.class);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String body = response.getBody();
        assertThat(body).isNotNull();
        List<Map<String, Object>> list = objectMapper.readValue(body, new TypeReference<>() {});
        assertThat(list).isNotEmpty();
        assertThat(body).contains("Best Developer", "Community Star");
    }

    @Test
    void getAwardById_shouldReturnPersistedAward() {
        // create one award and get its ID
        ResponseEntity<@NotNull String> created = client().create(AwardFactory.awardBestDeveloper2025(), jsonHeaders());
        assertThat(created.getStatusCode().is2xxSuccessful()).isTrue();
        Map<String, Object> createdDto = objectMapper.readValue(created.getBody(), new TypeReference<>() {});
        Number id = (Number) createdDto.get("id");

        // act
        ResponseEntity<@NotNull String> response = rest.getForEntity(baseUrl() + "/" + id.longValue(), String.class);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).contains("Best Developer", "Awarded for outstanding contributions", "2025");
    }

    @Test
    void getAwardById_shouldReturn404_whenNotExists() {
        long unknownId = 9_999_999L;
        ResponseEntity<@NotNull String> response = rest.getForEntity(baseUrl() + "/" + unknownId, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404", "\"path\":\"/awards/" + unknownId + "\"");
    }

    @Test
    void updateAward_shouldPersistChanges() {
        // arrange: create existing award
        ResponseEntity<@NotNull String> created = client().create(AwardFactory.awardBestDeveloper2025(), jsonHeaders());
        Map<String, Object> dto = objectMapper.readValue(created.getBody(), new TypeReference<>() {});
        long id = ((Number) dto.get("id")).longValue();

        AwardInputDTO update = new AwardInputDTO("Updated Title", "Updated Description", 2030);
        HttpEntity<@NotNull AwardInputDTO> request = new HttpEntity<>(update, jsonHeaders());

        // act
        ResponseEntity<@NotNull String> response = rest.exchange(
                baseUrl() + "/" + id, HttpMethod.PUT, request, String.class);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Updated Title", "Updated Description", "2030");
    }

    @Test
    void updateAward_shouldReturn404_whenNotExists() {
        long unknownId = 888888L;
        AwardInputDTO update = new AwardInputDTO("Any", "Any", 2020);
        HttpEntity<@NotNull AwardInputDTO> request = new HttpEntity<>(update, jsonHeaders());

        ResponseEntity<@NotNull String> response = rest.exchange(
                baseUrl() + "/" + unknownId, HttpMethod.PUT, request, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404", "\"error\":\"Recurso não encontrado\"");
    }

    @Test
    void updateAward_shouldReturn422_whenTitleIsBlank() {
        // arrange existing
        ResponseEntity<@NotNull String> created = client().create(AwardFactory.awardBestDeveloper2025(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();

        AwardInputDTO invalid = new AwardInputDTO("", "Updated", 2030);
        HttpEntity<@NotNull AwardInputDTO> request = new HttpEntity<>(invalid, jsonHeaders());

        // act
        ResponseEntity<@NotNull String> response = rest.exchange(
                baseUrl() + "/" + id, HttpMethod.PUT, request, String.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(422);
        assertThat(response.getBody()).contains("\"status\":422",
                "\"error\":\"Erro de validação\"",
                "\"name\":\"title\"");
    }

    @Test
    void deleteAward_shouldReturn204_andRemoveEntity() {
        // create
        ResponseEntity<@NotNull String> created = client().create(AwardFactory.awardBestDeveloper2025(), jsonHeaders());
        long id = ((Number) objectMapper.readValue(created.getBody(), new TypeReference<Map<String, Object>>() {})
                .get("id")).longValue();

        // delete
        ResponseEntity<@NotNull Void> response = rest.exchange(baseUrl() + "/" +
                id, HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
        assertThat(response.getStatusCode().value()).isEqualTo(204);

        // ensure gone
        ResponseEntity<@NotNull String> getAfterDelete = rest.getForEntity(baseUrl() + "/" + id, String.class);
        assertThat(getAfterDelete.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deleteAward_shouldReturn404_whenNotExists() {
        long unknownId = 123456789L;
        ResponseEntity<@NotNull String> response = rest.exchange(baseUrl() + "/" +
                unknownId, HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("\"status\":404");
    }

}