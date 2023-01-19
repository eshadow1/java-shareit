package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemDto addItem(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета: " + itemDto);

        return ItemMapper.toItemDto(itemService.addItem(ItemMapper.fromItemDto(itemDto, userId)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @PathVariable int id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление предмета " + id + ": " + itemDto);

        return ItemMapper.toItemDto(itemService.updateItem(id, ItemMapper.fromItemDto(itemDto, userId)));
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id) {
        log.info("Получен запрос на получение предмета " + id);

        return ItemMapper.toItemDto(itemService.getItem(id));
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на получение всех предметов");

        return itemService.getAllItemsByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ItemDto removeItem(@PathVariable int id) {
        log.info("Получен запрос на удаление предмета " + id);

        return ItemMapper.toItemDto(itemService.removeItem(id));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                     @RequestParam(defaultValue = "") String text) {
        log.info("Получен запрос на поиск " + text);

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(userId, text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
