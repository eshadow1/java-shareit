package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ReceivedItemDto;
import ru.practicum.shareit.item.dto.SentItemDto;

public final class ItemMapper {
    public static Item fromReceivedItemDto(final ReceivedItemDto receivedItemDto, final int userId) {
        return Item.builder()
                .description(receivedItemDto.getDescription())
                .available(receivedItemDto.getAvailable())
                .name(receivedItemDto.getName())
                .owner(userId)
                .build();
    }

    public static SentItemDto toSentItemDto(final Item item) {
        return SentItemDto.builder()
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
