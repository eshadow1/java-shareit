package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета: " + itemDto);

        return itemClient.addItem(userId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addItemComment(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @PathVariable int id,
                                     @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос на добавление коментария к предмету: " + id);

        return itemClient.addComment(userId,id, comment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                              @PathVariable int id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление предмета " + id + ": " + itemDto);

        return itemClient.updateItem(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                           @PathVariable int id) {
        log.info("Получен запрос на получение предмета " + id);

        return itemClient.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Получен запрос на получение всех предметов");

        return itemClient.getAllItemsByUser(userId, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable long id) {
        log.info("Получен запрос на удаление предмета " + id);

        return itemClient.removeItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @RequestParam(defaultValue = "") String text,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Получен запрос на поиск " + text);

        if (text.isEmpty() || text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return itemClient.searchItems(userId, text, from, size);
    }

}
