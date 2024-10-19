package ru.clevertec.session.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.session.repository.SessionRepository;
import ru.clevertec.session.service.ServiceException;
import ru.clevertec.session.service.SessionKiller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class NextDaySessionKiller implements SessionKiller {
    private final SessionRepository sessionRepository;

    @Override
    @PostConstruct
    public void kill() {
        new Thread(() ->
        {
            while (true) {
                sessionRepository.deleteAll();
                try {
                    Thread.sleep(getTimeUntilTomorrow());
                } catch (InterruptedException e) {
                    throw new ServiceException("Session killer is broken", e);
                }
            }
        }).start();
    }

    private long getTimeUntilTomorrow() {
        long timeNextDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        return timeNextDay - LocalDateTime.now()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
    }
}
