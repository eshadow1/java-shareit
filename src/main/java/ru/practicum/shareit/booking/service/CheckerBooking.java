package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.exception.*;

import java.util.Objects;

public class CheckerBooking {

    public static void checkedUpdate(Booking booking) {
        if (!Status.WAITING.equals(booking.getStatus())) {
            throw new UpdateFalseException("Предмет с id " + booking.getId() + " уже был обновлен");
        }
    }

    public static void checkedBookingContains(BookingRepository bookingRepository, int id) {
        if (bookingRepository.findById(id).isEmpty()) {
            throw new ContainsFalseException("Заказ с id " + id + " не найден");
        }
    }

    public static void checkedItemContains(ItemService itemStorage, Integer itemId) {
        if (!itemStorage.contains(itemId)) {
            throw new ContainsFalseException("Предмет с id " + itemId + " не найден");
        }
    }

    public static void checkedUserContains(UserRepository userRepository, Integer bookerId) {
        if (userRepository.findById(bookerId).isEmpty()) {
            throw new ContainsFalseException("Пользователь с id " + bookerId + " не найден");
        }
    }

    public static void checkItemAvailable(ItemService itemStorage, Integer itemId) {
        if (!itemStorage.get(itemId).getIsAvailable()) {
            throw new AvailableFalseException("Предмет с id " + itemId + " занят");
        }
    }

    public static void checkedItemBookingByUser(Item item, Integer userId) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotOwnerException("Предмет с id " + item.getId() + " не принадлежит пользователю " + userId);
        }
    }

    public static void checkedTimeBooking(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new TimeFalseException("Некорретные значения времени");
        }
    }

    public static void checkedBookingContainsUsers(int userId, BookingDao booking, Item item) {
        if (booking.getBooker().getId() != userId && item.getOwner().getId() != userId) {
            throw new ContainsFalseException("Заказ с id " + booking.getId() + " не связан с пользователями");
        }
    }

    public static void checkedBookerEqualsOwner(Integer bookerId, Integer owner) {
        if (bookerId.equals(owner)) {
            throw new ContainsFalseException("У заказа одинаковый пользователь и заказчик");
        }
    }

    private CheckerBooking() {
    }
}
