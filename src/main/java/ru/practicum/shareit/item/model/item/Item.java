package ru.practicum.shareit.item.model.item;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.model.comment.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(name = "owner_id")
    private Integer owner;

    @Column(name = "request_id")
    private Integer request;

    @Transient
    private BookingItemDao lastBooking;

    @Transient
    private BookingItemDao nextBooking;

    @Transient
    private List<Comment> comments;
}