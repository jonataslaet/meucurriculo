package com.meucurriculo.testsupport.clients;

import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.testsupport.HttpJsonClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class HardSkillProjectClient {
    private final HttpJsonClient http;
    private final String base;

    public HardSkillProjectClient(HttpJsonClient http, String baseUrl) {
        this.http = http;
        this.base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
    }

    public ResponseEntity<String> create(long projectId, HardSkillProjectInputDTO dto, HttpHeaders headers) {
        return http.post(base + "/projects/" + projectId + "/hardskills", headers, dto);
    }

    public ResponseEntity<String> list(long projectId) {
        return http.get(base + "/projects/" + projectId + "/hardskills");
    }
}
