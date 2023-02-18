package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.item.ItemDao;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.dto.UserDao;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void fromBookingDto() {
        var bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();
        var booking = BookingMapper.fromBookingDto(bookingDto, 1);
        var correctBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();
        assertEquals(booking.getStart(), correctBooking.getStart());
        assertEquals(booking.getEnd(), correctBooking.getEnd());
        assertEquals(booking.getItemId(), correctBooking.getItemId());
    }

    @Test
    void toBookingDto() {
        var booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        var bookingDto = BookingMapper.toBookingDto(booking);

        var correctBookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        assertEquals(bookingDto.getId(), correctBookingDto.getId());
        assertEquals(bookingDto.getStart(), correctBookingDto.getStart());
        assertEquals(bookingDto.getEnd(), correctBookingDto.getEnd());
    }

    @Test
    void fromBookingDao() {
        var itemDao = ItemDao.builder()
                .id(1)
                .name("test")
                .build();
        var userDao = UserDao.builder()
                .id(1)
                .build();

        var bookingDao = BookingDao.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .item(itemDao)
                .booker(userDao)
                .status(Status.WAITING)
                .build();

        var booking = BookingMapper.fromBookingDao(bookingDao);

        var correctBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        assertEquals(booking.getId(), correctBooking.getId());
        assertEquals(booking.getStart(), correctBooking.getStart());
        assertEquals(booking.getEnd(), correctBooking.getEnd());
        assertEquals(booking.getBookerId(), correctBooking.getBookerId());
    }

    @Test
    void toBookingDao() {
        var booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .build();

        var item = Item.builder()
                .id(1)
                .name("test")
                .description("description")
                .isAvailable(false)
                .build();

        var bookingDao = BookingMapper.toBookingDao(booking, item);
        var itemDao = ItemDao.builder()
                .id(1)
                .name("test")
                .build();
        var correctBooking = BookingDao.builder()
                .id(1)
                .start(LocalDateTime.of(2021, 1, 1, 1, 0, 1))
                .end(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .item(itemDao)
                .booker(null)
                .status(Status.WAITING)
                .build();

        assertEquals(bookingDao.getId(), correctBooking.getId());
        assertEquals(bookingDao.getStart(), correctBooking.getStart());
        assertEquals(bookingDao.getEnd(), correctBooking.getEnd());
    }
}