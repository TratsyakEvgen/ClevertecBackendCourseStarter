package ru.clevertec.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.session.dto.CreateSession;
import ru.clevertec.session.entity.Session;
import ru.clevertec.session.service.SessionService;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public Session get(@RequestParam String login) {
        return sessionService.get(login);
    }

    @PostMapping
    public Session create(@RequestBody CreateSession createSession) {
        return sessionService.create(createSession);
    }
}
