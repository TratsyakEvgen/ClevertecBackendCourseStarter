package ru.clevertec.starter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Session {
    private String id;
    private String login;
    private LocalDateTime creatDate;
}
