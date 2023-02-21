package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.db.FromPageRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ItemRequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Item item;

    private User user;

    private User user2;

    private ItemRequest itemRequest;

    @BeforeEach
    void beforEach() {
        user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        user2 = User.builder()
                .id(2)
                .name("Test2")
                .email("test2@test.test")
                .build();
        item = Item.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель")
                .isAvailable(true)
                .owner(user)
                .build();
        itemRequest = ItemRequest.builder()
                .id(1)
                .description("test")
                .requestorId(user)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void findItemRequestByRequestorId() {
        var itemRequests = itemRequestRepository.findItemRequestByRequestorId(user);

        assertEquals(itemRequests.size(), 1);
        assertEquals(itemRequests.get(0).getId(), itemRequest.getId());
        assertEquals(itemRequests.get(0).getDescription(), itemRequest.getDescription());
    }

    @Test
    void findItemRequestByRequestorIdNot() {
        var itemRequests = itemRequestRepository.findItemRequestByRequestorIdNot(user2,
                new FromPageRequest(0, 1, Sort.by(Sort.Direction.ASC, "created")));

        assertEquals(itemRequests.getContent().size(), 1);
    }
}