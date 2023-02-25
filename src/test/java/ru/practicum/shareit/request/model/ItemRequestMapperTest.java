package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void fromItemRequestDto() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        var itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("test")
                .requestorId(1)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();
        var itemRequest = ItemRequestMapper.fromItemRequestDto(itemRequestDto, user);

        var correctItemRequest = ItemRequest.builder()
                .id(1)
                .description("test")
                .requestorId(user)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();

        assertEquals(itemRequest.getRequestorId(), correctItemRequest.getRequestorId());
        assertEquals(itemRequest.getDescription(), correctItemRequest.getDescription());
        assertEquals(itemRequest.getCreated(), correctItemRequest.getCreated());
    }

    @Test
    void toItemRequestDto() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        var itemRequest = ItemRequest.builder()
                .id(1)
                .description("test")
                .requestorId(user)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();

        var itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        var correctItemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("test")
                .requestorId(1)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();

        assertEquals(itemRequestDto.getRequestorId(), correctItemRequestDto.getRequestorId());
        assertEquals(itemRequestDto.getDescription(), correctItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getCreated(), correctItemRequestDto.getCreated());
    }
}