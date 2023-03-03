package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequest) {
        log.info("Получен запрос на добавление запроса " + itemRequest + " для пользователя " + userId);

        return requestClient.addItemRequest(userId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение всех своих запросов для пользователя " + userId);

        return requestClient.getAllItemRequestByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @PathVariable int id) {
        log.info("Получен запрос на получение запроса с " + id + " от пользователя " + userId);

        return requestClient.getItemRequest(id, userId);
    }

    @GetMapping("/all")
    @Validated
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "1") @Positive int size) {
        log.info("Получен запрос на получение запросов с " + from + " размером " + size + " от пользователя " + userId);

        return requestClient.getItemRequestFromSize(userId, from, size);
    }
}
