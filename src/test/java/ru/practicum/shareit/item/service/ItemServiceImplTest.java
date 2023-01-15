package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserStorage;
import ru.practicum.shareit.utils.GeneratorId;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceImplTest {
    private ItemServiceImpl itemService;
    private Item correctItem;
    private Item noCorrectItem;
    private Item updateCorrectItem;
    private Item checkCorrectItem;

    @BeforeEach
    public void beforeEach() {
        var userStorage = new InMemoryUserStorage(new GeneratorId());
        var correctUser = User.builder()
                .name("Test")
                .email("test@test.com")
                .build();
        var correctUser2 = User.builder()
                .name("Test2")
                .email("test2@test.com")
                .build();
        userStorage.add(correctUser);
        userStorage.add(correctUser2);

        itemService = new ItemServiceImpl(new InMemoryItemStorage(new GeneratorId()),
                userStorage);

        correctItem = Item.builder()
                .name("Test")
                .description("Test")
                .available(true)
                .owner(1)
                .build();
        noCorrectItem = Item.builder()
                .name("Test")
                .description("Test")
                .available(true)
                .owner(10)
                .build();
        updateCorrectItem = Item.builder()
                .id(1)
                .name("Test")
                .description("Test des")
                .available(true)
                .owner(1)
                .build();

        checkCorrectItem = Item.builder()
                .id(1)
                .name("Test")
                .description("Test")
                .available(true)
                .owner(1)
                .build();
    }

    @Test
    void addCorrectItem() {
        itemService.addItem(correctItem);
        assertEquals(checkCorrectItem.getId(), itemService.getItem(checkCorrectItem.getId()).getId());
    }

    @Test
    void addNoCorrectItem() {
        assertThrows(UserNotFoundException.class,
                () -> itemService.addItem(noCorrectItem));
    }

    @Test
    void updateItem() {
        itemService.addItem(correctItem);
        itemService.updateItem(checkCorrectItem.getId(), updateCorrectItem);
        assertEquals(updateCorrectItem.getName(), itemService.getItem(checkCorrectItem.getId()).getName());
        assertEquals(updateCorrectItem.getDescription(), itemService.getItem(checkCorrectItem.getId()).getDescription());
        assertEquals(updateCorrectItem.getOwner(), itemService.getItem(checkCorrectItem.getId()).getOwner());
    }

    @Test
    void updateIncorrectItem() {
        itemService.addItem(correctItem);
        var updateIncorrectItem = Item.builder()
                .id(1)
                .name("Test")
                .description("Test des")
                .available(true)
                .owner(2)
                .build();

        assertThrows(ContainsFalseException.class,
                () -> itemService.updateItem(checkCorrectItem.getId(), updateIncorrectItem));
    }

    @Test
    void getItem() {
        itemService.addItem(correctItem);
        assertEquals(checkCorrectItem.getName(), itemService.getItem(checkCorrectItem.getId()).getName());
        assertEquals(checkCorrectItem.getDescription(), itemService.getItem(checkCorrectItem.getId()).getDescription());
        assertEquals(checkCorrectItem.getOwner(), itemService.getItem(checkCorrectItem.getId()).getOwner());
    }

    @Test
    void getNoItem() {
        assertThrows(ContainsFalseException.class,
                () -> itemService.getItem(checkCorrectItem.getId()));
    }

    @Test
    void getAllItemsByUser() {
        itemService.addItem(correctItem);
        int size = 1;
        assertEquals(size, itemService.getAllItemsByUser(correctItem.getOwner()).size());
    }

    @Test
    void getAllItemsByOtherUser() {
        itemService.addItem(correctItem);
        int size = 0;
        assertEquals(size, itemService.getAllItemsByUser(2).size());
    }

    @Test
    void removeItem() {
        itemService.addItem(correctItem);
        itemService.removeItem(checkCorrectItem.getId());
        assertThrows(ContainsFalseException.class,
                () -> itemService.getItem(checkCorrectItem.getId()));
    }

    @Test
    void searchItems() {
        itemService.addItem(correctItem);
        int size = 1;
        assertEquals(size, itemService.searchItems(correctItem.getOwner(), "test").size());
    }

    @Test
    void searchNoItems() {
        itemService.addItem(correctItem);
        int size = 0;
        assertEquals(size, itemService.searchItems(correctItem.getOwner(), "asd").size());
    }
}