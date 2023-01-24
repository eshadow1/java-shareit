package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {
    List<Booking> getAll();

    Booking get(int userId);

    Booking add(Booking user);

    Booking remove(Booking user);

    Booking remove(int userId);

    Booking update(Booking user);

    boolean contains(int userId);
}
