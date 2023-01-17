package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ReceivedItemDto;
import ru.practicum.shareit.item.dto.SentItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SentItemDto addItem(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                               @Valid @RequestBody ReceivedItemDto receivedItemDto) {
        log.info("Получен запрос на добавление предмета: " + receivedItemDto);

        return ItemMapper.toSentItemDto(itemService.addItem(ItemMapper.fromReceivedItemDto(receivedItemDto, userId)));
    }

    @PatchMapping("/{id}")
    public SentItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                  @PathVariable int id, @RequestBody ReceivedItemDto receivedItemDto) {
        log.info("Получен запрос на обновление предмета " + id + ": " + receivedItemDto);

        return ItemMapper.toSentItemDto(itemService.updateItem(id, ItemMapper.fromReceivedItemDto(receivedItemDto, userId)));
    }

    @GetMapping("/{id}")
    public SentItemDto getItem(@PathVariable int id) {
        log.info("Получен запрос на получение предмета " + id);

        return ItemMapper.toSentItemDto(itemService.getItem(id));
    }

    @GetMapping
    public List<SentItemDto> getItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на получение всех предметов");

        return itemService.getAllItemsByUser(userId).stream()
                .map(ItemMapper::toSentItemDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public SentItemDto removeItem(@PathVariable int id) {
        log.info("Получен запрос на удаление предмета " + id);

        return ItemMapper.toSentItemDto(itemService.removeItem(id));
    }

    @GetMapping("/search")
    public List<SentItemDto> searchItems(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                         @RequestParam(defaultValue = "") String text) {
        log.info("Получен запрос на поиск " + text);

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(userId, text).stream()
                .map(ItemMapper::toSentItemDto)
                .collect(Collectors.toList());
    }
}
