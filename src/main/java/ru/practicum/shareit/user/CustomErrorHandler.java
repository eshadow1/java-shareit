package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class CustomErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleContainsFalseException(final ContainsFalseException error) {
        return Map.of("error", HttpStatus.NOT_FOUND + ": " + error.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleContainsTrueException(final ContainsTrueException error) {
        return Map.of("error", HttpStatus.CONFLICT + ": " + error.getMessage());
    }
}
