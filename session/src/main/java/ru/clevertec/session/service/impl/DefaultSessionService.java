package ru.clevertec.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.session.dto.CreateSession;
import ru.clevertec.session.entity.Session;
import ru.clevertec.session.repository.SessionRepository;
import ru.clevertec.session.service.ServiceException;
import ru.clevertec.session.service.SessionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefaultSessionService implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public Session get(String login) {
        return sessionRepository.findByLogin(login)
                .orElseThrow(() -> new ServiceException("Session not found"));
    }

    @Override
    public Session create(CreateSession createSession) {
        if (createSession == null) {
            throw new ServiceException("Create session is null");
        }
        String login = createSession.getLogin();

        return sessionRepository.findByLogin(login)
                .orElseGet(() -> {
                            Session session = new Session()
                                    .setLogin(login)
                                    .setCreatDate(LocalDateTime.now());
                            return sessionRepository.save(session);
                        }
                );
    }
}
