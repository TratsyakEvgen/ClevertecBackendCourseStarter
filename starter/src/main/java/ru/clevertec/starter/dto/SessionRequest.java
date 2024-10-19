package ru.clevertec.starter.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SessionRequest {
    private final String login;
    private Session session;
}
