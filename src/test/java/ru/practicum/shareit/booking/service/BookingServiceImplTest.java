package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private static User user;

    private static User user2;

    private static User user3;

    private static Item item;

    private static Booking correctBooking;

    private static Booking updateCorrectBooking;


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

        user3 = User.builder()
                .id(3)
                .name("user3")
                .email("user3@user.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("test")
                .description("test test")
                .isAvailable(true)
                .owner(user)
                .build();

        correctBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(item.getId())
                .bookerId(user2.getId())
                .status(Status.WAITING)
                .build();

        updateCorrectBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(item.getId())
                .bookerId(user2.getId())
                .status(Status.APPROVED)
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        userService.addUser(user);
        userService.addUser(user2);
        userService.addUser(user3);

        itemService.addItem(item);
    }

    @Test
    void addBooking() {
        var booking = bookingService.addBooking(correctBooking);

        assertEquals(booking.getId(), correctBooking.getId());
        assertEquals(booking.getStart(), correctBooking.getStart());
        assertEquals(booking.getEnd(), correctBooking.getEnd());
        assertEquals(booking.getStatus(), correctBooking.getStatus());
    }

    @Test
    void updateBooking() {
        bookingService.addBooking(correctBooking);
        var booking = bookingService.updateBooking(1, 1, true);

        assertEquals(booking.getId(), correctBooking.getId());
        assertEquals(booking.getStart(), correctBooking.getStart());
        assertEquals(booking.getEnd(), correctBooking.getEnd());
        assertEquals(booking.getStatus(), updateCorrectBooking.getStatus());
    }

    @Test
    void getBooking() {
        bookingService.addBooking(correctBooking);
        var booking = bookingService.getBooking(1, 1);

        assertEquals(booking.getId(), correctBooking.getId());
        assertEquals(booking.getStart(), correctBooking.getStart());
        assertEquals(booking.getEnd(), correctBooking.getEnd());
    }

    @Test
    void getAllBookingUser() {
        bookingService.addBooking(correctBooking);
        var booking = bookingService.getAllBookingUser(user2.getId(), State.ALL, 0, 1);

        assertEquals(booking.size(), 1);
        assertEquals(booking.get(0).getId(), correctBooking.getId());
        assertEquals(booking.get(0).getStart(), correctBooking.getStart());
        assertEquals(booking.get(0).getEnd(), correctBooking.getEnd());
    }

    @Test
    void getAllBookingOwner() {
        bookingService.addBooking(correctBooking);
        var booking = bookingService.getAllBookingOwner(user.getId(), State.ALL, 0, 1);

        assertEquals(booking.size(), 1);
        assertEquals(booking.get(0).getId(), correctBooking.getId());
        assertEquals(booking.get(0).getStart(), correctBooking.getStart());
        assertEquals(booking.get(0).getEnd(), correctBooking.getEnd());
    }

}