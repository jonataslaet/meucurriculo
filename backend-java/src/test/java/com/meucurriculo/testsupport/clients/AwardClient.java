package com.meucurriculo.testsupport.clients;

import com.meucurriculo.controllers.dtos.AwardInputDTO;
import com.meucurriculo.testsupport.HttpJsonClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class AwardClient {
    private final HttpJsonClient http;
    private final String base;

    public AwardClient(HttpJsonClient http, String baseUrl) {
        this.http = http;
        this.base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
    }

    public ResponseEntity<String> create(AwardInputDTO dto, HttpHeaders headers) {
        return http.post(base + "/awards", headers, dto);
    }

    public ResponseEntity<String> listAll() {
        return http.get(base + "/awards");
    }
}
