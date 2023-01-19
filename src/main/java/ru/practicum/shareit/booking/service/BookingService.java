package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.util.List;

@Service
public class BookingService {

    private final BookingStorage bookingStorage;

    public BookingService(@Qualifier("inMemory") BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    public Booking addBooking(Booking booking) {
        return bookingStorage.add(booking);
    }

    public Booking updateBooking(int id, Booking booking) {
        checkedBookingContains(id);
        return bookingStorage.update(booking);
    }


    public Booking getBooking(int bookingId) {
        checkedBookingContains(bookingId);

        return bookingStorage.get(bookingId);
    }

    public List<Booking> getAllBooking() {
        return bookingStorage.getAll();
    }

    public Booking removeBooking(int bookingId) {
        return bookingStorage.remove(bookingId);
    }

    private void checkedBookingContains(int id) {
        if (!bookingStorage.contains(id)) {
            throw new ContainsFalseException("Заказ с id " + id + " не найден");
        }
    }
}
