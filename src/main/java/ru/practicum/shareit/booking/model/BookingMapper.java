package ru.practicum.shareit.booking.model;


import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.item.ItemDao;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.dto.UserDao;

public class BookingMapper {
    public static Booking fromBookingDto(final BookingDto bookingDto,
                                         final Integer bookerId) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(bookingDto.getItemId())
                .status(bookingDto.getStatus())
                .bookerId(bookerId)
                .build();
    }

    public static BookingDto toBookingDto(final Booking item) {
        return BookingDto.builder()
                .id(item.getId())
                .itemId(item.getItemId())
                .bookerId(item.getBookerId())
                .start(item.getStart())
                .end(item.getEnd())
                .status(item.getStatus())
                .build();
    }

    public static Booking update(Booking oldBooking, boolean approved) {
        var bufItem = oldBooking.toBuilder();

        if (approved) {
            bufItem.status(Status.APPROVED);
        } else {
            bufItem.status(Status.REJECTED);
        }

        return bufItem.build();
    }

    public static Booking fromBookingDao(BookingDao bookingDao) {
        return Booking.builder()
                .id(bookingDao.getId())
                .start(bookingDao.getStart())
                .end(bookingDao.getEnd())
                .itemId(bookingDao.getItem().getId())
                .status(bookingDao.getStatus())
                .bookerId(bookingDao.getBooker().getId())
                .build();
    }

    public static BookingDao toBookingDao(Booking booking, Item item) {
        return BookingDao.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemDao(item.getId(), item.getName()))
                .status(booking.getStatus())
                .booker(new UserDao(booking.getBookerId()))
                .build();
    }

    private BookingMapper() {
    }
}
