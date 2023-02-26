package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.exception.AddFalseException;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.UserNotFoundException;

import java.util.Optional;

public class CheckerItem {

    public static void checkedAddComment(Optional<Comment> addComment, int itemId) {
        if (addComment.isEmpty()) {
            throw new AddFalseException("Коментарий к вещи id " + itemId + " не добавлен");
        }
    }

    public static void checkedItemContains(ItemRepository itemRepository, int id) {
        if (itemRepository.findById(id).isEmpty()) {
            throw new ContainsFalseException("Предмет с id " + id + " не найден");
        }
    }

    public static void checkedItemForUser(int id, int owner) {
        if (id != owner) {
            throw new ContainsFalseException("Предмет с id " + id + " не принадлежит пользователю " + owner);
        }
    }

    public static void checkedUserContains(UserRepository userRepository, int owner) {
        if (userRepository.findById(owner).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + owner + " не найден");
        }
    }

    private CheckerItem() {
    }
}
