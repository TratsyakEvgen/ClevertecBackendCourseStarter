package ru.clevertec.starter.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.starter.dto.Session;
import ru.clevertec.starter.dto.SessionRequest;
import ru.clevertec.starter.service.SessionSupplier;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class HttpSessionSupplier implements SessionSupplier {
    private final RestTemplate restTemplate;
    private final String url;

    @Override
    public Optional<Session> getSession(SessionRequest sessionRequest) {
        Session session;
        try {
            session = restTemplate.getForObject(url + "?login=" + sessionRequest.getLogin(), Session.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.info(e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(session);
    }

    @Override
    public Optional<Session> crateSession(SessionRequest sessionRequest) {
        Session session;
        try {
            session = restTemplate.postForObject(url, sessionRequest, Session.class);
        } catch (RestClientException e) {
            log.info(e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(session);
    }
}
