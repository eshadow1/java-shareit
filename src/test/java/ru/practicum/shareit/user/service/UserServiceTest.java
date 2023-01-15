package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserStorage;
import ru.practicum.shareit.utils.GeneratorId;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private User correctUser;
    private User updateCorrectUser;
    private User checkCorrectUser;

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(new InMemoryUserStorage(new GeneratorId()));
        correctUser = User.builder()
                .name("Test")
                .email("test@test.com")
                .build();
        checkCorrectUser = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.com")
                .build();
        updateCorrectUser = User.builder()
                .id(1)
                .name("TestUpdate")
                .email("test@test.com")
                .build();
    }

    @Test
    void addUser() {
        userService.addUser(correctUser);
        assertEquals(checkCorrectUser, userService.getUser(checkCorrectUser.getId()));
    }

    @Test
    void updateUser() {
        userService.addUser(correctUser);
        userService.updateUser(checkCorrectUser.getId(), updateCorrectUser);
        assertEquals(updateCorrectUser.getName(), userService.getUser(checkCorrectUser.getId()).getName());
    }

    @Test
    void updateNoUser() {
        assertThrows(ContainsFalseException.class,
                () -> userService.updateUser(checkCorrectUser.getId(), updateCorrectUser));
    }

    @Test
    void getAllUsers() {
        userService.addUser(correctUser);
        int size = 1;
        assertEquals(size, userService.getAllUsers().size());
    }

    @Test
    void getNoUser() {
        assertThrows(ContainsFalseException.class,
                () -> userService.getUser(10));
    }

    @Test
    void removeUser() {
        userService.addUser(correctUser);
        userService.removeUser(checkCorrectUser.getId());
        int size = 0;
        assertEquals(size, userService.getAllUsers().size());
    }
}