package ru.practicum.shareit.request.model;

import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequest fromItemRequestDto(final ItemRequestDto itemDto,
                                                 final User requestor) {
        return ItemRequest.builder()
                .id(itemDto.getId())
                .description(itemDto.getDescription())
                .requestorId(requestor)
                .created(itemDto.getCreated())
                .items(null)
                .build();
    }

    public static ItemRequestDto toItemRequestDto(final ItemRequest item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestorId(item.getRequestorId().getId())
                .created(item.getCreated())
                .items(item.getItems() == null ? null :
                        item.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList()))
                .build();
    }

    private ItemRequestMapper() {
    }
}