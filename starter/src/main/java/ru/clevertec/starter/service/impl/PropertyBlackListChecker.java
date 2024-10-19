package ru.clevertec.starter.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.starter.exception.SessionException;
import ru.clevertec.starter.service.BlackListChecker;

import java.util.List;

@RequiredArgsConstructor
public class PropertyBlackListChecker implements BlackListChecker {
    private final List<String> logins;

    @Override
    public void check(String login) {
        if (logins.contains(login)) {
            throw new SessionException("Forbidden for " + login);
        }
    }
}
