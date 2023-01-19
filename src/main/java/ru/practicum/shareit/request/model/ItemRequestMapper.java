package ru.practicum.shareit.request.model;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {
    public static ItemRequest fromItemRequestDto(final ItemRequestDto itemDto,
                                                 final int itemId,
                                                 final User requestor) {
        return ItemRequest.builder()
                .id(itemId)
                .description(itemDto.getDescription())
                .requestor(requestor)
                .created(itemDto.getCreated())
                .build();
    }

    public static ItemRequestDto toBookingDto(final ItemRequest item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestorId(item.getRequestor().getId())
                .created(item.getCreated())
                .build();
    }

    private ItemRequestMapper() {
    }
}