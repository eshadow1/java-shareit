package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item);

    Item updateItem(int id, Item item);

    Item getItem(int userId);

    List<Item> getAllItemsByUser(int itemId);

    Item removeItem(int itemId);

    List<Item> searchItems(int userId, String text);

    Comment addComment(Comment comment);

    Item get(int itemId);

    List<Item> getAllByUser(int userId);

    boolean contains(Integer itemId);
}
