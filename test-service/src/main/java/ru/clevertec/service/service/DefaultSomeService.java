package ru.clevertec.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.starter.annotation.Sessional;
import ru.clevertec.starter.dto.SessionRequest;
import ru.clevertec.starter.service.impl.PropertyBlackListChecker;

@Service
@Slf4j
public class DefaultSomeService implements SomeService {
    @Override
    @Sessional(blackLists = PropertyBlackListChecker.class)
    public void doSome(SessionRequest sessionRequest) {
        log.info(sessionRequest.getSession().toString());
    }
}
