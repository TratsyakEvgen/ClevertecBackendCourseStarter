package ru.clevertec.session.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.clevertec.session.entity.Session;

import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, Long> {
    Optional<Session> findByLogin(String login);
}
