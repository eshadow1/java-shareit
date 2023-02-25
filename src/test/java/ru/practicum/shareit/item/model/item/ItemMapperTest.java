package ru.practicum.shareit.item.model.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void fromItemDto() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        var itemDto = ItemDto.builder()
                .id(1)
                .name("test")
                .description("description")
                .available(false)
                .ownerId(1)
                .requestId(1)
                .build();
        var item = ItemMapper.fromItemDto(itemDto, user);

        var correctItem = Item.builder()
                .id(1)
                .name("test")
                .description("description")
                .isAvailable(false)
                .owner(user)
                .build();

        assertEquals(item.getDescription(), correctItem.getDescription());
        assertEquals(item.getName(), correctItem.getName());
        assertEquals(item.getIsAvailable(), correctItem.getIsAvailable());
    }

    @Test
    void fromItemDtoWithItemRequest() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        var itemRequest = ItemRequest.builder()
                .id(1)
                .build();
        var itemDto = ItemDto.builder()
                .id(1)
                .name("test")
                .description("description")
                .available(false)
                .ownerId(1)
                .requestId(1)
                .build();
        var item = ItemMapper.fromItemDto(itemDto, user, itemRequest);

        var correctItem = Item.builder()
                .id(1)
                .name("test")
                .description("description")
                .isAvailable(false)
                .owner(user)
                .itemRequest(itemRequest)
                .lastBooking(new BookingItemDao())
                .nextBooking(new BookingItemDao())
                .build();

        assertEquals(item.getDescription(), correctItem.getDescription());
        assertEquals(item.getName(), correctItem.getName());
        assertEquals(item.getIsAvailable(), correctItem.getIsAvailable());
    }

    @Test
    void toItemDto() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        var nextBooking = BookingItemDao.builder()
                .id(3)
                .bookerId(1)
                .build();
        var lastBooking = BookingItemDao.builder()
                .id(2)
                .bookerId(2)
                .build();
        var item = Item.builder()
                .id(1)
                .name("test")
                .description("description")
                .isAvailable(false)
                .owner(user)
                .itemRequest(null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
        var itemDto = ItemMapper.toItemDto(item);

        var correctItemDto = ItemDto.builder()
                .id(1)
                .name("test")
                .description("description")
                .available(false)
                .ownerId(1)
                .build();

        assertEquals(itemDto.getId(), correctItemDto.getId());
        assertEquals(itemDto.getDescription(), correctItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), correctItemDto.getAvailable());
        assertEquals(itemDto.getOwnerId(), correctItemDto.getOwnerId());
    }
}