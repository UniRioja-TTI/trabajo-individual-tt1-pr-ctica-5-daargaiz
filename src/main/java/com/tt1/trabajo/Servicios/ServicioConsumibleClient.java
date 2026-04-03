package com.tt1.trabajo.Servicios;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelo.DatosSolicitud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ServicioConsumibleClient {

    private final RestClient restClient;
    private final String usuario;

    private static final String[] NOMBRES_PARAM_USUARIO = { "user", "username", "usuario" };
    private static final String[] NOMBRES_PARAM_TOKEN = { "tok", "token" };

    public ServicioConsumibleClient(
            @Value("${servicio.consumible.base-url}") String baseUrl,
            @Value("${servicio.usuario}") String usuario) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.usuario = usuario;
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

    public int solicitarSimulation(DatosSolicitud solicitud) {
        for (String nombreParam : NOMBRES_PARAM_USUARIO) {
            try {
                String respuesta = restClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/Solicitud/Solicitar")
                                .queryParam(nombreParam, usuario)
                                .build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(solicitud)
                        .retrieve()
                        .body(String.class);

                return extraerPrimerEntero(respuesta);
            } catch (RestClientResponseException ignored) {
            }
        }
        return -1;
    }

    public String descargarResultados(int tok) {
        for (String nombreParam : NOMBRES_PARAM_TOKEN) {
            try {
                return restClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/Resultados")
                                .queryParam(nombreParam, tok)
                                .build())
                        .retrieve()
                        .body(String.class);
            } catch (RestClientResponseException ignored) {
            }
        }
        return "";
    }

    private int extraerPrimerEntero(String texto) {
        if (texto == null || texto.isBlank()) {
            return -1;
        }

        Matcher matcher = Pattern.compile("-?\\d+").matcher(texto);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return -1;
    }
}