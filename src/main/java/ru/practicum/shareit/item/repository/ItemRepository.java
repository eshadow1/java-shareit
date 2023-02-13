package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i " +
            "FROM Item AS i " +
            "WHERE i.isAvailable = true AND (lower(i.name) LIKE lower(?1)  OR lower(i.description) LIKE lower(?1)) " +
            "ORDER BY i.id")
    List<Item> searchItemsBy(String tempText);

    List<Item> findByOwnerOrderByIdAsc(User user);

    List<Item> findByIdAndOwnerOrderByIdAsc(int itemId, int userId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingItemDao(b.id, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = ?1 AND b.bookerId = ?2 AND b.start < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<BookingItemDao> findAllLastBookingByItemId(int itemId, int bookerId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingItemDao(b.id, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = ?1 AND b.bookerId = ?2 AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start ASC")
    List<BookingItemDao> findAllFutureBookingByItemId(int itemId, int bookerId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingItemDao(b.id, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = ?1 AND b.start < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<BookingItemDao> findAllLastBookingByItemId(int itemId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingItemDao(b.id, b.bookerId) " +
            "FROM Booking AS b " +
            "WHERE b.itemId = ?1 AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start ASC")
    List<BookingItemDao> findAllFutureBookingByItemId(int itemId);

    @Query("SELECT u " +
            "FROM User AS u " +
            "WHERE u.id = ?1 ")
    Optional<User> getUser(int userId);
}
