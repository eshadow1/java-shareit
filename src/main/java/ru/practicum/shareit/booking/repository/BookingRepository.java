package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select i from Item as i where i.id = ?1")
    Optional<Item> getItem(Integer itemId);

    List<Booking> findByBookerIdOrderByStartDesc(int userId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(int userId, Status status);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime timeAfter, LocalDateTime timeBefore);

    List<Booking> findByItemIdInOrderByStartDesc(List<Integer> items);

    List<Booking> findByItemIdInAndStatusOrderByStartDesc(List<Integer> items, Status status);

    List<Booking> findByItemIdInAndStartAfterOrderByStartDesc(List<Integer> items, LocalDateTime time);

    List<Booking> findByItemIdInAndEndBeforeOrderByStartDesc(List<Integer> items, LocalDateTime time);

    List<Booking> findByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(List<Integer> items, LocalDateTime timeAfter, LocalDateTime timeBefore);
}
