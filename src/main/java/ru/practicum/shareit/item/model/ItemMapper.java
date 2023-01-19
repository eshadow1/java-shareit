package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

public final class ItemMapper {
    public static Item fromItemDto(final ItemDto itemDto, final int userId) {
        return Item.builder()
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .owner(userId)
                .build();
    }

    public static ItemDto toItemDto(final Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
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

        if (newItem.getAvailable() != null) {
            bufItem.available(newItem.getAvailable());
        }

        return bufItem.build();
    }

    private ItemMapper() {
    }
}
