package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    public ItemRequestController(ItemRequestService itemRequestService,
                                 UserService userService) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                         @Valid @RequestBody ItemRequestDto itemRequest) {
        log.info("Получен запрос на добавление запроса " + itemRequest + " для пользователя " + userId);

        return ItemRequestMapper.toItemRequestDto(itemRequestService.addItemRequest(ItemRequestMapper.fromItemRequestDto(itemRequest.toBuilder()
                        .created(LocalDateTime.now())
                        .build(),
                userService.getUser(userId))));
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequests(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на получение всех своих запросов для пользователя " + userId);

        return itemRequestService.getAllItemRequestByUser(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemRequestDto getItemRequest(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                         @PathVariable int id) {
        log.info("Получен запрос на получение запроса с " + id + " от пользователя " + userId);

        return ItemRequestMapper.toItemRequestDto(itemRequestService.getItemRequest(userId, id));
    }

    @GetMapping("/all")
    @Validated
    public List<ItemRequestDto> getAllItemRequest(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "1") @Positive int size) {
        log.info("Получен запрос на получение запросов с " + from + " размером " + size + " от пользователя " + userId);

        return itemRequestService.getItemRequestFromSize(userId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}
