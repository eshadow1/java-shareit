package ru.practicum.shareit.request.repository;


import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage {
    List<ItemRequest> getAll();

    ItemRequest get(int userId);

    ItemRequest add(ItemRequest user);

    ItemRequest remove(ItemRequest user);

    ItemRequest remove(int userId);

    ItemRequest update(ItemRequest user);

    boolean contains(int userId);
}
