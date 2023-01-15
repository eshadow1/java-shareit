package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item user);

    Item updateItem(int id, Item user);

    Item getItem(int userId);

    List<Item> getAllItemsByUser(int userId);

    Item removeItem(int userId);

    List<Item> searchItems(int userId, String text);
}