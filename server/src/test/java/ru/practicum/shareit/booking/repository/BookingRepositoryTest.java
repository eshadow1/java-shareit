package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User user;
    private Booking booking;

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
        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        userRepository.save(user);
        itemRepository.save(item);
    }


    @Test
    void getItem() {
        var getItem = bookingRepository.getItem(item.getId());

        assertEquals(getItem.get().getId(), item.getId());
        assertEquals(getItem.get().getName(), item.getName());
        assertEquals(getItem.get().getDescription(), item.getDescription());
    }
}