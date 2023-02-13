package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT new ru.practicum.shareit.item.model.comment.Comment(c.id, c.text, c.item, c.author, u.name) " +
            "FROM Comment AS c " +
            "JOIN User As u ON c.author.id = u.id " +
            "WHERE c.item.id = (?1) ")
    List<Comment> findAllByItemId(Integer itemId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.itemId = (?1) AND b.bookerId = (?2)  AND b.status = 'APPROVED' AND b.end < CURRENT_TIMESTAMP ")
    List<Booking> getBookingByItemIdAndAuthorId(Integer itemId, Integer authorId);
}
