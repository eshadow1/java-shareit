package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

public class MappingItem {
    static public Item fromItemDTO(ItemDto itemDto, int userId) {
        return Item.builder()
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .owner(userId)
                .build();
    }

    public static Item update(Item item, Item newItem) {
        var bufItem = item.toBuilder();
        if (newItem.getName() != null && !newItem.getName().isEmpty()) {
            bufItem.name(newItem.getName());
        }

        if (newItem.getDescription() != null && !newItem.getDescription().isEmpty()) {
            bufItem.description(newItem.getDescription());
        }

        if (newItem.getAvailable() != null) {
            bufItem.available(newItem.getAvailable());
        }

        return bufItem.build();
    }
}
