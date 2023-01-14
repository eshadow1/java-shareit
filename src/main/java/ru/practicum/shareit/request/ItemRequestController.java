package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ResponseEntity<ItemRequest> addItemRequest(@Valid @RequestBody ItemRequest itemRequest) {
        log.info("Получен запрос на добавление запроса: " + itemRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestService.addItemRequest(itemRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemRequest> updateItemRequest(@PathVariable int id, @RequestBody ItemRequest itemRequest) {
        log.info("Получен запрос на обновление запроса " + id + ": " + itemRequest);

        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.updateItemRequest(id, itemRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequest> getItemRequest(@PathVariable int id) {
        log.info("Получен запрос на получение запроса " + id);

        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.getItemRequest(id));
    }

    @GetMapping
    public List<ItemRequest> getItemRequests() {
        log.info("Получен запрос на получение всех запросов");
        return itemRequestService.getAllItemRequest();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemRequest> removeUser(@PathVariable int id) {
        log.info("Получен запрос на удаление запроса " + id);

        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.removeItemRequest(id));
    }
}
