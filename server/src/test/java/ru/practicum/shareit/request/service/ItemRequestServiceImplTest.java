package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    private static User user;

    private static User user2;

    private static ItemRequest correctItemRequest;

    private static ItemRequest correctItemRequest2;

    private static ItemRequest incorrectItemRequest;


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

        correctItemRequest = ItemRequest.builder()
                .id(1)
                .description("itemTest")
                .requestorId(user)
                .created(LocalDateTime.of(2022,1,1,1,1))
                .build();

        correctItemRequest2 = ItemRequest.builder()
                .id(1)
                .description("itemTest2")
                .requestorId(user2)
                .created(LocalDateTime.of(2022,1,1,1,1))
                .build();

        incorrectItemRequest = ItemRequest.builder()
                .id(1)
                .description("itemTest")
                .requestorId(user)
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        userService.addUser(user);
    }

    @Test
    void addIncorrectItemRequest() {
        assertThrows(
                RuntimeException.class,
                () -> itemRequestService.addItemRequest(incorrectItemRequest));
    }

    @Test
    void addItemRequest() {
        var itemRequest = itemRequestService.addItemRequest(correctItemRequest);

        assertEquals(itemRequest.getId(), correctItemRequest.getId());
        assertEquals(itemRequest.getDescription(), correctItemRequest.getDescription());

    }

    @Test
    void addItemRequestWithoutUser() {
        assertThrows(
                ContainsFalseException.class,
                () -> itemRequestService.addItemRequest(correctItemRequest2));
    }

    @Test
    void getItemRequest() {
        itemRequestService.addItemRequest(correctItemRequest);

        var itemRequest = itemRequestService.getItemRequest(user.getId(), 1);

        assertEquals(itemRequest.getId(), correctItemRequest.getId());
        assertEquals(itemRequest.getDescription(), correctItemRequest.getDescription());
        assertEquals(itemRequest.getRequestorId(), correctItemRequest.getRequestorId());
    }

    @Test
    void getAllItemRequestByUser() {
        itemRequestService.addItemRequest(correctItemRequest);

        var itemRequest = itemRequestService.getAllItemRequestByUser(user.getId());

        assertEquals(itemRequest.size(), 1);
        assertEquals(itemRequest.get(0).getId(), correctItemRequest.getId());
        assertEquals(itemRequest.get(0).getDescription(), correctItemRequest.getDescription());
        assertEquals(itemRequest.get(0).getRequestorId(), correctItemRequest.getRequestorId());
    }

    @Test
    void getItemRequestFromSizeOtherUser() {
        userService.addUser(user2);
        itemRequestService.addItemRequest(correctItemRequest);

        var itemRequest = itemRequestService.getItemRequestFromSize(user2.getId(), 0, 1);

        assertEquals(itemRequest.size(), 1);
        assertEquals(itemRequest.get(0).getId(), correctItemRequest.getId());
        assertEquals(itemRequest.get(0).getDescription(), correctItemRequest.getDescription());
        assertEquals(itemRequest.get(0).getRequestorId(), correctItemRequest.getRequestorId());
    }

    @Test
    void getItemRequestFromSize() {
        itemRequestService.addItemRequest(correctItemRequest);

        var itemRequest = itemRequestService.getItemRequestFromSize(user.getId(), 0, 1);

        assertEquals(itemRequest.size(), 0);
    }
}