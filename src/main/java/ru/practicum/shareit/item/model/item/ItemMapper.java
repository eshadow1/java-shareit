package ru.practicum.shareit.item.model.item;

import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.user.model.User;

public final class ItemMapper {
    public static Item fromItemDto(final ItemDto itemDto, final User user) {
        return Item.builder()
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .name(itemDto.getName())
                .owner(user)
                .build();
    }

    public static ItemDto toItemDto(final Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .ownerId(item.getOwner().getId())
                .requestId(item.getRequest())
                .lastBooking(item.getLastBooking())
                .nextBooking(item.getNextBooking())
                .comments(item.getComments())
                .build();
    }

    public static Item update(final Item item, final Item newItem) {
        var bufItem = item.toBuilder();
        if (newItem.getName() != null && !newItem.getName().isEmpty()) {
            bufItem.name(newItem.getName());
        }

        if (newItem.getDescription() != null
                && !newItem.getDescription().isEmpty()) {
            bufItem.description(newItem.getDescription());
        }

        if (newItem.getIsAvailable() != null) {
            bufItem.isAvailable(newItem.getIsAvailable());
        }

        return bufItem.build();
    }

    private ItemMapper() {
    }
}
