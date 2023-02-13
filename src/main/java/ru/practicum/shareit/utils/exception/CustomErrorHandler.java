package ru.practicum.shareit.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.UserController;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class,
        ItemController.class,
        ItemRequestController.class,
        BookingController.class})
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundException(final UserNotFoundException error) {
        return Map.of("error", HttpStatus.NOT_FOUND + ": " + error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotOwnerException(final NotOwnerException error) {
        return Map.of("error", error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAvailableFalseException(final AvailableFalseException error) {
        return Map.of("error", HttpStatus.BAD_REQUEST + ": " + error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTimeFalseException(final TimeFalseException error) {
        return Map.of("error", HttpStatus.BAD_REQUEST + ": " + error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalStateException(final IllegalStateException error) {
        return Map.of("error", error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUpdateFalseException(final UpdateFalseException error) {
        return Map.of("error", error.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAddFalseException(final AddFalseException error) {
        return Map.of("error", HttpStatus.BAD_REQUEST + ": " + error.getMessage());
    }

}
