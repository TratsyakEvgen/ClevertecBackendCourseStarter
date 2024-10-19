package ru.clevertec.session.service;

import ru.clevertec.session.dto.CreateSession;
import ru.clevertec.session.entity.Session;

public interface SessionService {
    Session get(String login);

    Session create(CreateSession createSession);

}
