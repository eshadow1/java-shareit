package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.item.Item;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select i from Item as i where i.id = ?1")
    Optional<Item> getItem(Integer itemId);

    @Query("select b from Booking as b where b.bookerId = ?1 order by b.start DESC")
    List<Booking> findByBookerId(int userId);

    @Query("select b from Booking as b where b.itemId in ?1 order by b.start DESC")
    List<Booking> findByItems(List<Integer> items);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Booking b SET b.status = ?2 WHERE b.id = ?1")
    void updateStatus(int id, Status status);
}
