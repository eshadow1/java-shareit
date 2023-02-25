package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Booking> findByBookerId(int userId, Pageable pageable);

    Page<Booking> findByBookerIdAndStatus(int userId, Status status, Pageable pageable);

    Page<Booking> findByBookerIdAndStartAfter(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndEndBefore(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfter(int userId, LocalDateTime timeAfter, LocalDateTime timeBefore, Pageable pageable);

    Page<Booking> findByItemIdIn(List<Integer> items, Pageable pageable);

    Page<Booking> findByItemIdInAndStatus(List<Integer> items, Status status, Pageable pageable);

    Page<Booking> findByItemIdInAndStartAfter(List<Integer> items, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItemIdInAndEndBefore(List<Integer> items, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItemIdInAndStartBeforeAndEndAfter(List<Integer> items, LocalDateTime timeAfter, LocalDateTime timeBefore, Pageable pageable);
}
