package ru.clevertec.starter.service;

import ru.clevertec.starter.dto.Session;
import ru.clevertec.starter.dto.SessionRequest;

import java.util.Optional;

public interface SessionSupplier {
    Optional<Session> getSession(SessionRequest sessionRequest);

    Optional<Session> crateSession(SessionRequest sessionRequest);
}
