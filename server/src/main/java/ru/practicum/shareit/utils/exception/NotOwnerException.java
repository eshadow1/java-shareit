package ru.practicum.shareit.utils.exception;

public class NotOwnerException  extends RuntimeException {
    public NotOwnerException(String message) {
        super(message);
    }
}