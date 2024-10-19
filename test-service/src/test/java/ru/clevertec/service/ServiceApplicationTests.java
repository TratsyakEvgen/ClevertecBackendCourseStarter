package ru.clevertec.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.clevertec.service.service.SomeService;
import ru.clevertec.starter.dto.Session;
import ru.clevertec.starter.dto.SessionRequest;
import ru.clevertec.starter.exception.SessionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(port = 8080, name = "session"))
class ServiceApplicationTests {
    @Autowired
    private SomeService someService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void doSome_ifLoginInBlackList() {
        SessionRequest sessionRequest = new SessionRequest("blackListLogin");
        SessionException sessionException = Assertions.assertThrows(SessionException.class, () -> someService.doSome(sessionRequest));
        Assertions.assertEquals("Forbidden for blackListLogin", sessionException.getMessage());
    }

    @Test
    void doSome_ifSessionNotExists() throws IOException {
        String login = "login";

        WireMock.stubFor(WireMock.get("/sessions?login=" + login).willReturn(WireMock.notFound()));
        WireMock.stubFor(WireMock.post("/sessions")
                .withRequestBody(WireMock.equalToJson(read("__files/sessionRequest.json")))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read("__files/session.json")))
        );
        SessionRequest sessionRequest = new SessionRequest(login);
        someService.doSome(sessionRequest);
        Session expected = objectMapper.readValue(read("__files/session.json"), Session.class);
        Assertions.assertEquals(expected, sessionRequest.getSession());

    }

    @Test
    void doSome_ifSessionExists() throws IOException {
        String login = "login";
        WireMock.stubFor(WireMock.get("/sessions?login=" + login)
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read("__files/session.json"))));
        SessionRequest sessionRequest = new SessionRequest(login);
        someService.doSome(sessionRequest);
        Session expected = objectMapper.readValue(read("__files/session.json"), Session.class);
        Assertions.assertEquals(expected, sessionRequest.getSession());

    }

    private String read(String path) throws IOException {
        return Files.readString(Path.of(new ClassPathResource(path).getURI()));
    }


}
