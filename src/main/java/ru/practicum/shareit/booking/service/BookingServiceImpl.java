package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.utils.exception.*;

import java.util.List;
import java.util.Objects;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public BookingServiceImpl(@Qualifier("inDb") BookingStorage bookingStorage,
                              @Qualifier("inDb") ItemStorage itemStorage,
                              @Qualifier("inDb") UserStorage userStorage) {
        this.bookingStorage = bookingStorage;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public BookingDao addBooking(Booking booking) {
        checkedItemContains(booking.getItemId());
        checkItemAvailable(booking.getItemId());
        checkedUserContains(booking.getBookerId());
        checkedTimeBooking(booking);
        var item = itemStorage.get(booking.getItemId());
        checkedBookerEqualsOwner(booking.getBookerId(), item.getOwner().getId());

        booking.setStatus(Status.WAITING);
        return bookingStorage.add(booking);
    }

    @Override
    public BookingDao updateBooking(int id, int userId, boolean approved) {
        checkedBookingContains(id);
        checkedUserContains(userId);

        var oldBooking = BookingMapper.fromBookingDao(bookingStorage.get(id));
        checkedUpdate(oldBooking);


        var item = itemStorage.get(oldBooking.getItemId());
        checkedItemBookingByUser(item, userId);

        oldBooking = BookingMapper.update(oldBooking, approved);
        bookingStorage.update(oldBooking);
        return getBooking(id, userId);
    }

    @Override
    public BookingDao getBooking(int bookingId, int userId) {
        checkedBookingContains(bookingId);
        checkedUserContains(userId);

        var booking = bookingStorage.get(bookingId);
        var item = itemStorage.get(booking.getItem().getId());

        checkedBookingContainsUsers(userId, booking, item);

        return booking;
    }

    @Override
    public List<BookingDao> getAllBookingUser(int userId, State state) {
        checkedUserContains(userId);
        return bookingStorage.getAllByUserAndState(userId, state);
    }

    @Override
    public List<BookingDao> getAllBookingOwner(int userId, State state) {
        checkedUserContains(userId);
        var items = itemStorage.getAllByUser(userId);
        return bookingStorage.getAllByItems(items, state);
    }

    @Override
    public BookingDao removeBooking(int bookingId) {
        return bookingStorage.remove(bookingId);
    }

    private void checkedUpdate(Booking booking) {
        if (!Status.WAITING.equals(booking.getStatus())) {
            throw new UpdateFalseException("Предмет с id " + booking.getId() + " уже был обновлен");
        }
    }

    private void checkedBookingContains(int id) {
        if (!bookingStorage.contains(id)) {
            throw new ContainsFalseException("Заказ с id " + id + " не найден");
        }
    }

    private void checkedItemContains(Integer itemId) {
        if (!itemStorage.contains(itemId)) {
            throw new ContainsFalseException("Предмет с id " + itemId + " не найден");
        }
    }

    private void checkedUserContains(Integer bookerId) {
        if (!userStorage.contains(bookerId)) {
            throw new ContainsFalseException("Пользователь с id " + bookerId + " не найден");
        }
    }

    private void checkItemAvailable(Integer itemId) {
        if (!itemStorage.get(itemId).getIsAvailable()) {
            throw new AvailableFalseException("Предмет с id " + itemId + " занят");
        }
    }

    private void checkedItemBookingByUser(Item item, Integer userId) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotOwnerException("Предмет с id " + item.getId() + " не принадлежит пользователю " + userId);
        }
    }

    private void checkedTimeBooking(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new TimeFalseException("Некорретные значения времени");
        }
    }

    private void checkedBookingContainsUsers(int userId, BookingDao booking, Item item) {
        if (booking.getBooker().getId() != userId && item.getOwner().getId() != userId) {
            throw new ContainsFalseException("Заказ с id " + booking.getId() + " не связан с пользователями");
        }
    }

    private void checkedBookerEqualsOwner(Integer bookerId, Integer owner) {
        if (bookerId.equals(owner)) {
            throw new ContainsFalseException("У заказа одинаковый пользователь и заказчик");
        }
    }
}
