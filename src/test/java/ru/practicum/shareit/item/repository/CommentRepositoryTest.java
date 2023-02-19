package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Comment comment;

    private Booking booking;

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

        comment = Comment.builder()
                .text("test")
                .item(item)
                .author(user)
                .authorName(user.getName())
                .build();

        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.APPROVED)
                .build();

        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        commentRepository.save(comment);
    }

    @Test
    void findAllByItemId() {
        var comments = commentRepository.findAllByItemId(item.getId());

        assertEquals(comments.size(), 1);
        assertEquals(comments.get(0).getText(), comment.getText());
    }

    @Test
    void getBookingByItemIdAndAuthorId() {
        var bookings = commentRepository.getBookingByItemIdAndAuthorId(item.getId(), user.getId());

        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getId(), booking.getId());
    }
}