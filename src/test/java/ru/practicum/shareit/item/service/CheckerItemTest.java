package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.utils.exception.AddFalseException;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CheckerItemTest {

    @Test
    void checkedAddComment() {
        Optional<Comment> addCommen = Optional.of(new Comment());
        int itemId = 1;

        Assertions.assertDoesNotThrow(() -> {
            CheckerItem.checkedAddComment(addCommen, itemId);
        });
    }

    @Test
    void checkedItemForUser() {
        int userId = 1;

        Assertions.assertDoesNotThrow(() -> {
            CheckerItem.checkedItemForUser(userId, userId);
        });
    }

    @Test
    void checkedUserContains() {

    }

    @Test
    void checkedAddCommentWithThrow() {
        Optional<Comment> addCommen = Optional.empty();
        int itemId = 1;

        assertThrows(AddFalseException.class, () -> {
            CheckerItem.checkedAddComment(addCommen, itemId);
        });
    }

    @Test
    void checkedItemForUserWithThrow() {
        int userId = 1;
        int otherId = 2;
        assertThrows(ContainsFalseException.class, () -> {
            CheckerItem.checkedItemForUser(userId, otherId);
        });
    }
}