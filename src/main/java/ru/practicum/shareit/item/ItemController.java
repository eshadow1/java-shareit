package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.MappingItem;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Item> addItem(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                        @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета: " + itemDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.addItem(MappingItem.fromItemDTO(itemDto, userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @PathVariable int id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление предмета " + id + ": " + itemDto);


        return ResponseEntity.status(HttpStatus.OK).body(itemService.updateItem(id, MappingItem.fromItemDTO(itemDto, userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable int id) {
        log.info("Получен запрос на получение предмета " + id);

        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItem(id));
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на получение всех предметов");
        return itemService.getAllItemsByUser(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> removeItem(@PathVariable int id) {
        log.info("Получен запрос на удаление предмета " + id);

        return ResponseEntity.status(HttpStatus.OK).body(itemService.removeItem(id));
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                  @RequestParam(defaultValue = "") String text) {
        log.info("Получен запрос на поиск " + text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemService.searchItems(userId, text);
    }

}
