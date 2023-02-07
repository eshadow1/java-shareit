package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.item.Item;

import java.time.LocalDateTime;
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
        var tempBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        tempBooking.setStatus(booking.getStatus());
        bookingRepository.save(tempBooking);
        return get(booking.getId());
    }

    @Override
    public boolean contains(int bookingId) {
        return bookingRepository.findById(bookingId).isPresent();
    }

    @Override
    public List<BookingDao> getAllByItems(List<Item> items, State state) {
        var allItem = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> allBooking;
        var time = LocalDateTime.now();
        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByItemIdInOrderByStartDesc(allItem);
                break;
            case WAITING:
                allBooking = bookingRepository.findByItemIdInAndStatusOrderByStartDesc(allItem, Status.WAITING);
                break;
            case REJECTED:
                allBooking = bookingRepository.findByItemIdInAndStatusOrderByStartDesc(allItem, Status.REJECTED);
                break;
            case PAST:
                allBooking = bookingRepository.findByItemIdInAndEndBeforeOrderByStartDesc(allItem, time);
                break;
            case FUTURE:
                allBooking = bookingRepository.findByItemIdInAndStartAfterOrderByStartDesc(allItem, time);
                break;
            case CURRENT:
                allBooking = bookingRepository.findByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(allItem, time, time);
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

        return allBooking.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDao> getAllByUserAndState(int userId, State state) {
        List<Booking> allBooking;
        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case WAITING:
                allBooking = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                allBooking = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            case PAST:
                allBooking = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                allBooking = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case CURRENT:
                var time = LocalDateTime.now();
                allBooking = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time);
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

        return allBooking.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }
}
