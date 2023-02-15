package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.model.comment.CommentMapper;
import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exception.BadNumberException;

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
    private final UserService userService;
    private final ItemRequestServiceImpl itemRequestService;

    public ItemController(ItemService itemService,
                          UserService userService,
                          ItemRequestServiceImpl itemRequestService) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление предмета: " + itemDto);

        return ItemMapper.toItemDto(itemService.addItem((itemDto.getRequestId() != null)
                ? ItemMapper.fromItemDto(itemDto, userService.getUser(userId), itemRequestService.getItemRequest(userId, itemDto.getRequestId()))
                : ItemMapper.fromItemDto(itemDto, userService.getUser(userId))));
    }

    @PostMapping("/{id}/comment")
    public CommentDto addItemComment(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                     @PathVariable int id,
                                     @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос на добавление коментария к предмету: " + id);

        return CommentMapper.toCommentDto(itemService.addComment(
                CommentMapper.fromCommentDto(comment,
                        userService.getUser(userId),
                        itemService.getItem(id))));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @PathVariable int id, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление предмета " + id + ": " + itemDto);

        return ItemMapper.toItemDto(itemService.updateItem(id, (itemDto.getRequestId() != null)
                ? ItemMapper.fromItemDto(itemDto, userService.getUser(userId), itemRequestService.getItemRequest(userId, itemDto.getRequestId()))
                : ItemMapper.fromItemDto(itemDto, userService.getUser(userId))));
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                           @PathVariable int id) {
        log.info("Получен запрос на получение предмета " + id);


        var item = itemService.getItem(id);

        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (itemDto.getOwnerId() != userId) {
            return itemDto.toBuilder().lastBooking(null).nextBooking(null).build();
        }

        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "20") int size) {
        log.info("Получен запрос на получение всех предметов");

        if (from < 0)
            throw new BadNumberException("from меньше 0");
        if (size <= 0)
            throw new BadNumberException("size меньше 0");

        return itemService.getAllItemsByUser(userId, from / size, size).stream()
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
                                     @RequestParam(defaultValue = "") String text,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "20") int size) {
        log.info("Получен запрос на поиск " + text);

        if (from < 0)
            throw new BadNumberException("from меньше 0");
        if (size <= 0)
            throw new BadNumberException("size меньше 0");

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(userId, text, from / size, size).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
