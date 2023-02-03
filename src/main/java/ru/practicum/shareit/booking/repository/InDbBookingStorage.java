package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.item.model.item.Item;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("inDb")
public class InDbBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;

    public InDbBookingStorage(@Lazy BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<BookingDao> getAll() {
        var all = bookingRepository.findAll();
        return all.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDao get(int bookingId) {
        var temp = bookingRepository.findById(bookingId).orElse(null);
        return temp != null ? BookingMapper.toBookingDao(temp,
                bookingRepository.getItem(temp.getItemId()).orElse(null)) : null;
    }

    @Override
    public BookingDao add(Booking booking) {
        var tempBooking = bookingRepository.save(booking);
        return get(tempBooking.getId());
    }

    @Override
    public BookingDao remove(Booking booking) {
        var oldBooking = get(booking.getId());
        bookingRepository.delete(booking);
        return oldBooking;
    }

    @Override
    public BookingDao remove(int bookingId) {
        var booking = get(bookingId);
        bookingRepository.deleteById(bookingId);
        return booking;
    }

    @Override
    public BookingDao update(Booking booking) {
        bookingRepository.updateStatus(booking.getId(), booking.getStatus());
        return get(booking.getId());
    }

    @Override
    public boolean contains(int bookingId) {
        return bookingRepository.findById(bookingId).isPresent();
    }

    @Override
    public List<BookingDao> getAllByUser(int userId) {
        var all = bookingRepository.findByBookerId(userId);
        return all.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDao> getAllByItems(List<Item> items) {
        var all = bookingRepository.findByItems(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return all.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }
}
