package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserStorage;
import ru.practicum.shareit.utils.GeneratorId;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;
    private User correctUser;
    private UserDto updateCorrectUser;
    private User checkCorrectUser;

    @BeforeEach
    public void beforeEach() {
        userServiceImpl = new UserServiceImpl(new InMemoryUserStorage(new GeneratorId()));
        correctUser = User.builder()
                .name("Test")
                .email("test@test.com")
                .build();
        checkCorrectUser = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.com")
                .build();
        updateCorrectUser = UserDto.builder()
                .name("TestUpdate")
                .email("test@test.com")
                .build();
    }

    @Test
    void addUser() {
        userServiceImpl.addUser(correctUser);
        assertEquals(checkCorrectUser, userServiceImpl.getUser(checkCorrectUser.getId()));
    }

    @Test
    void updateUser() {
        userServiceImpl.addUser(correctUser);
        userServiceImpl.updateUser(checkCorrectUser.getId(), updateCorrectUser);
        assertEquals(updateCorrectUser.getName(), userServiceImpl.getUser(checkCorrectUser.getId()).getName());
    }

    @Test
    void updateNoUser() {
        assertThrows(ContainsFalseException.class,
                () -> userServiceImpl.updateUser(checkCorrectUser.getId(), updateCorrectUser));
    }

    @Test
    void getAllUsers() {
        userServiceImpl.addUser(correctUser);
        int size = 1;
        assertEquals(size, userServiceImpl.getAllUsers().size());
    }

    @Test
    void getNoUser() {
        assertThrows(ContainsFalseException.class,
                () -> userServiceImpl.getUser(10));
    }

    @Test
    void removeUser() {
        userServiceImpl.addUser(correctUser);
        userServiceImpl.removeUser(checkCorrectUser.getId());
        int size = 0;
        assertEquals(size, userServiceImpl.getAllUsers().size());
    }
}