package com.meucurriculo.testsupport;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient; // Spring 6+ (or use RestTemplate)

public class HttpJsonClient {

    private final RestClient client;

    public HttpJsonClient() {
        this.client = RestClient.create(); // simple, no extras
    }

    public ResponseEntity<String> post(String url, HttpHeaders headers, Object body) {
        return client.post().uri(url).headers(h -> h.addAll(headers)).body(body).retrieve().toEntity(String.class);
    }

    public ResponseEntity<String> get(String url) {
        return client.get().uri(url).retrieve().toEntity(String.class);
    }
}
