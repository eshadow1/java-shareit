package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.utils.GeneratorId;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryItemStorageTest {
    private InMemoryItemStorage itemStorage;
    private Item correctItem;
    private Item updateCorrectItem;
    private Item checkCorrectItem;

    @BeforeEach
    public void beforeEach() {
        itemStorage = new InMemoryItemStorage(new GeneratorId());
        correctItem = Item.builder()
                .name("Test")
                .description("Test")
                .isAvailable(true)
                .owner(1)
                .build();
        updateCorrectItem = Item.builder()
                .id(1)
                .name("Test")
                .description("Test des")
                .isAvailable(true)
                .owner(1)
                .build();
        checkCorrectItem = Item.builder()
                .id(1)
                .name("Test")
                .description("Test")
                .isAvailable(true)
                .owner(1)
                .build();
    }

    @Test
    void addCorrectItem() {
        itemStorage.add(correctItem);
        assertTrue(itemStorage.contains(checkCorrectItem.getId()));
    }

    @Test
    void removeCorrectItem() {
        itemStorage.add(correctItem);
        itemStorage.remove(checkCorrectItem);
        assertFalse(itemStorage.contains(checkCorrectItem.getId()));
    }

    @Test
    void removeCorrectItemById() {
        itemStorage.add(correctItem);
        itemStorage.remove(checkCorrectItem.getId());
        assertFalse(itemStorage.contains(checkCorrectItem.getId()));
    }

    @Test
    void update() {
        itemStorage.add(correctItem);
        itemStorage.update(updateCorrectItem);
        assertEquals(updateCorrectItem.getDescription(), itemStorage.get(updateCorrectItem.getId()).getDescription());
    }

    @Test
    void getAllByUser() {
        itemStorage.add(correctItem);
        int size = 1;
        assertEquals(size, itemStorage.getAllByUser(checkCorrectItem.getOwner()).size());
    }

    @Test
    void getAllByOtherUser() {
        itemStorage.add(correctItem);
        int size = 0;
        int otherOwner = 10;
        assertEquals(size, itemStorage.getAllByUser(otherOwner).size());
    }

    @Test
    void get() {
        itemStorage.add(correctItem);
        assertEquals(checkCorrectItem.getId(), itemStorage.get(checkCorrectItem.getId()).getId());
        assertEquals(checkCorrectItem.getName(), itemStorage.get(checkCorrectItem.getId()).getName());
        assertEquals(checkCorrectItem.getDescription(), itemStorage.get(checkCorrectItem.getId()).getDescription());
        assertEquals(checkCorrectItem.getIsAvailable(), itemStorage.get(checkCorrectItem.getId()).getIsAvailable());
    }

    @Test
    void searchItems() {
    }
}