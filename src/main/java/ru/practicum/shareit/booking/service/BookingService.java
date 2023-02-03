package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDao addBooking(Booking booking);

    BookingDao updateBooking(int id, int userId, boolean approved);


    BookingDao getBooking(int bookingId, int userId);

    List<BookingDao> getAllBookingUser(int userId, State state);

    List<BookingDao> getAllBookingOwner(int userId, State state);

    BookingDao removeBooking(int bookingId);
}
