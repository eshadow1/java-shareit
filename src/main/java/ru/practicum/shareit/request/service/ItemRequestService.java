package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.repository.ItemRequestStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.util.List;

@Service
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;

    public ItemRequestService(@Qualifier("inMemory") ItemRequestStorage itemRequestStorage) {
        this.itemRequestStorage = itemRequestStorage;
    }

    public ItemRequest addItemRequest(ItemRequest booking) {
        return itemRequestStorage.add(booking);
    }

    public ItemRequest updateItemRequest(int id, ItemRequest booking) {
        checkedItemRequestContains(id);
        return itemRequestStorage.update(booking);
    }


    public ItemRequest getItemRequest(int bookingId) {
        checkedItemRequestContains(bookingId);

        return itemRequestStorage.get(bookingId);
    }

    public List<ItemRequest> getAllItemRequest() {
        return itemRequestStorage.getAll();
    }

    public ItemRequest removeItemRequest(int bookingId) {
        return itemRequestStorage.remove(bookingId);
    }

    private void checkedItemRequestContains(int id) {
        if (!itemRequestStorage.contains(id)) {
            throw new ContainsFalseException("Заказ с id " + id + " не найден");
        }
    }
}
