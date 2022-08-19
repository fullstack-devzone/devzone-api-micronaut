package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.models.LinkDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.HttpStatus.OK;

@MicronautTest
public class LinkControllerTest {
    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void shouldGetLinks() {
        HttpRequest<?> request = HttpRequest.GET("/api/links");
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(OK, response.getStatus());
    }

    @Test
    void shouldSearchLinks() {
        HttpRequest<?> request = HttpRequest.GET("/api/links?query=spring");
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(OK, response.getStatus());
    }

    @Test
    void shouldGetLinksByTag() {
        HttpRequest<?> request = HttpRequest.GET("/api/links?tag=java");
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(OK, response.getStatus());
    }

    @Test
    void shouldCreateLink() {
        LinkDTO body = new LinkDTO();
        body.setTitle("SivaLabs");
        body.setUrl("https://sivalabs.in");
        body.setTags(List.of("java", "springboot"));
        HttpRequest<?> request = HttpRequest.POST("/api/links", body);
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());
    }
}