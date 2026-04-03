package com.tt1.trabajo.Servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ServicioConsumibleClient {

    private final RestClient restClient;

    public ServicioConsumibleClient(
            @Value("${servicio.consumible.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public String enviarEmail(String emailAddress, String message) {
        try {
            return restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/Email")
                            .queryParam("emailAddress", emailAddress)
                            .queryParam("message", message)
                            .build())
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Error " + e.getStatusCode() + ": " + e.getResponseBodyAsString();
        }
    }
}
