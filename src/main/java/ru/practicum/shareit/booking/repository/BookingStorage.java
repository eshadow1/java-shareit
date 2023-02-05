package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.model.item.Item;

import java.util.List;

public interface BookingStorage {
    List<BookingDao> getAll();

    BookingDao get(int bookingId);

    BookingDao add(Booking booking);

    BookingDao remove(Booking booking);

    BookingDao remove(int bookingId);

    BookingDao update(Booking booking);

    boolean contains(int bookingId);

    List<BookingDao> getAllByItems(List<Item> items, State state);

    List<BookingDao> getAllByUserAndState(int userId, State state);
}
