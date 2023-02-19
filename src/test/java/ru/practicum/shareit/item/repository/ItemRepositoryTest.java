package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User user;

    @BeforeEach
    void beforEach() {
        user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        item = Item.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель")
                .isAvailable(true)
                .owner(user)
                .build();

        userRepository.save(user);
        itemRepository.save(item);
    }


    @Test
    void searchItemsBy() {
        var items = itemRepository.searchItemsBy("Дрель",
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals(items.getContent().size(), 1);
        assertEquals(items.getContent().get(0).getId(), item.getId());
        assertEquals(items.getContent().get(0).getDescription(), item.getDescription());
    }

    @Test
    void findByOwnerOrderByIdAsc() {
        var items = itemRepository.findByOwnerOrderByIdAsc(user);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getDescription(), item.getDescription());
    }

    @Test
    void findByOwner() {
        var items = itemRepository.findByOwner(user,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals(items.getContent().size(), 1);
    }

    @Test
    void findAllLastBookingByItemId() {
        var items = itemRepository.findAllLastBookingByItemId(item.getId(), user.getId());

        assertEquals(items.size(), 0);
    }

    @Test
    void findAllFutureBookingByItemId() {
        var items = itemRepository.findAllFutureBookingByItemId(item.getId(), user.getId());

        assertEquals(items.size(), 0);
    }
}