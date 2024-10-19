package ru.clevertec.session.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "sessions")
public class Session {
    @Id
    private String id;
    private String login;
    private LocalDateTime creatDate;
}
