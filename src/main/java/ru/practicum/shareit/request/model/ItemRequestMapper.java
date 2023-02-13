package ru.practicum.shareit.request.model;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequest fromItemRequestDto(final ItemRequestDto itemDto,
                                                 final int itemId,
                                                 final Integer requestorId) {
        return ItemRequest.builder()
                .id(itemId)
                .description(itemDto.getDescription())
                .requestorId(requestorId)
                .created(itemDto.getCreated())
                .build();
    }

    public static ItemRequestDto toBookingDto(final ItemRequest item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestorId(item.getRequestorId())
                .created(item.getCreated())
                .build();
    }

    private ItemRequestMapper() {
    }
}