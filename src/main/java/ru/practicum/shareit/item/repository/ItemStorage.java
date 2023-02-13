package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getAllByUser(int userId);

    Item get(int itemId);

    Item add(Item item);

    Item remove(Item item);

    Item remove(int itemId);

    Item update(Item item);

    boolean contains(int itemId);

    List<Item> searchItems(String text);

    Optional<Comment> addComment(Comment comment);
}
