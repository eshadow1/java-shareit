package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exception.AddFalseException;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private static User user;

    private static User user2;

    private static Item correctItem;

    private static Item correctItem2;

    private static Item updateCorrectItem;

    private static Comment comment;

    @BeforeAll
    public static void beforeAll() {
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();

        user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@user.com")
                .build();

        correctItem = Item.builder()
                .id(1)
                .name("test")
                .description("test test")
                .isAvailable(false)
                .owner(user)
                .build();

        correctItem2 = Item.builder()
                .id(2)
                .name("test")
                .description("test test")
                .isAvailable(false)
                .owner(user2)
                .build();

        updateCorrectItem = Item.builder()
                .id(1)
                .name("update")
                .description("test test")
                .isAvailable(false)
                .owner(user)
                .build();

        comment = Comment.builder()
                .text("test")
                .item(correctItem)
                .author(user)
                .authorName(user.getName())
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        userService.addUser(user);
    }

    @Test
    void addItem() {
        var item = itemService.addItem(correctItem);

        assertEquals(item.getId(), correctItem.getId());
        assertEquals(item.getName(), correctItem.getName());
        assertEquals(item.getDescription(), correctItem.getDescription());
    }

    @Test
    void addItemWithoutUser() {
        assertThrows(
                UserNotFoundException.class,
                () -> itemService.addItem(correctItem2));
    }

    @Test
    void updateItem() {
        var item = itemService.addItem(correctItem);
        var updateItem = itemService.updateItem(item.getId(), updateCorrectItem);

        assertEquals(updateItem.getId(), updateCorrectItem.getId());
        assertEquals(updateItem.getName(), updateCorrectItem.getName());
        assertEquals(updateItem.getDescription(), updateCorrectItem.getDescription());
    }

    @Test
    void getItem() {
        itemService.addItem(correctItem);
        var item = itemService.getItem(correctItem.getId());

        assertEquals(item.getId(), correctItem.getId());
        assertEquals(item.getName(), correctItem.getName());
        assertEquals(item.getDescription(), correctItem.getDescription());
    }

    @Test
    void getItemNotFound() {
        assertThrows(ContainsFalseException.class,
                () -> itemService.getItem(correctItem.getId()));
    }

    @Test
    void getAllItemsByUser() {
        itemService.addItem(correctItem);
        var items = itemService.getAllItemsByUser(correctItem.getId(), 0, 1);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getId(), correctItem.getId());
        assertEquals(items.get(0).getName(), correctItem.getName());
        assertEquals(items.get(0).getDescription(), correctItem.getDescription());
    }

    @Test
    void removeItem() {
        itemService.addItem(correctItem);

        itemService.removeItem(correctItem.getId());

        assertFalse(itemService.contains(correctItem.getId()));
    }

    @Test
    void addComment() {
        userService.addUser(user);
        itemService.addItem(correctItem);

        assertThrows(AddFalseException.class,
                () -> itemService.addComment(comment));
    }

    @Test
    void searchItems() {
        userService.addUser(user);
        userService.addUser(user2);
        itemService.addItem(correctItem);

        var items = itemService.searchItems(user.getId(), "test", 0, 1);
        assertEquals(items.size(), 0);
    }

    @Test
    void getAllByUser() {
        itemService.addItem(correctItem);
        var items = itemService.getAllItemsByUser(correctItem.getId(), 0, 1);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getId(), correctItem.getId());
        assertEquals(items.get(0).getName(), correctItem.getName());
        assertEquals(items.get(0).getDescription(), correctItem.getDescription());
    }

    @Test
    void contains() {
        assertFalse(itemService.contains(correctItem.getId()));

        itemService.addItem(correctItem);

        assertTrue(itemService.contains(correctItem.getId()));
    }
}