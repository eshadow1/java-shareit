package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer ownerId;

    private Integer requestId;

    private BookingItemDao lastBooking;

    private BookingItemDao nextBooking;

    private List<CommentDto> comments;
}
