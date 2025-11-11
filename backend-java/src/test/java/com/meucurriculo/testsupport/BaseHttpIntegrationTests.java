package com.meucurriculo.testsupport;

import com.meucurriculo.testsupport.containers.PostgresTC;
import com.meucurriculo.repositories.AwardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseHttpIntegrationTests {

    @LocalServerPort
    protected int port;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AwardRepository awardRepository;

    @DynamicPropertySource
    static void dataSourceProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", PostgresTC.INSTANCE::getJdbcUrl);
        r.add("spring.datasource.username", PostgresTC.INSTANCE::getUsername);
        r.add("spring.datasource.password", PostgresTC.INSTANCE::getPassword);
    }

    protected String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    protected HttpHeaders jsonHeaders() {
        var h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    @BeforeEach
    void clearDatabase() {
        awardRepository.deleteAll();
    }
}
