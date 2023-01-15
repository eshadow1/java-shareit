package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.GeneratorId;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryUserStorageTest {
    private InMemoryUserStorage userStorage;
    private User correctUser;
    private User updateCorrectUser;
    private User checkCorrectUser;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage(new GeneratorId());
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
    void addCorrectUser() {
        userStorage.add(correctUser);
        assertTrue(userStorage.contains(checkCorrectUser.getId()));
    }

    @Test
    void removeCorrectUser() {
        userStorage.add(correctUser);
        userStorage.remove(checkCorrectUser);
        assertFalse(userStorage.contains(checkCorrectUser.getId()));
    }

    @Test
    void removeCorrectUserById() {
        userStorage.add(correctUser);
        userStorage.remove(checkCorrectUser.getId());
        assertFalse(userStorage.contains(checkCorrectUser.getId()));
    }

    @Test
    void updateCorrectUser() {
        userStorage.add(correctUser);
        userStorage.update(updateCorrectUser);
        assertEquals(updateCorrectUser.getId(), userStorage.get(updateCorrectUser.getId()).getId());
        assertEquals(updateCorrectUser.getName(), userStorage.get(updateCorrectUser.getId()).getName());
        assertEquals(updateCorrectUser.getEmail(), userStorage.get(updateCorrectUser.getId()).getEmail());
    }

    @Test
    void getAll() {
        userStorage.add(correctUser);
        int sizeStorage = 1;
        assertEquals(sizeStorage, userStorage.getAll().size());
    }

    @Test
    void get() {
        userStorage.add(correctUser);
        assertEquals(checkCorrectUser.getEmail(), userStorage.get(checkCorrectUser.getId()).getEmail());
        assertEquals(checkCorrectUser.getName(), userStorage.get(checkCorrectUser.getId()).getName());
    }


    @Test
    void isContains() {
        userStorage.add(correctUser);
        assertTrue(userStorage.contains(checkCorrectUser.getEmail()));
    }

    @Test
    void isNotContains() {
        userStorage.add(correctUser);
        assertFalse(userStorage.contains("text@text.com"));
    }
}