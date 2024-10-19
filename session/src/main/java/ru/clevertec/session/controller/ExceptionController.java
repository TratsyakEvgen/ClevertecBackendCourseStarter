package ru.clevertec.session.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.session.dto.Error;
import ru.clevertec.session.service.ServiceException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleServiceException(ServiceException e) {
        return new Error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
