package ru.practicum.shareit.booking.model;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking fromBookingDto(final BookingDto itemDto,
                                         final int bookingId,
                                         final Item item,
                                         final User booker) {
        return Booking.builder()
                .id(bookingId)
                .item(item)
                .booker(booker)
                .start(itemDto.getStart())
                .end(itemDto.getEnd())
                .build();
    }

    public static BookingDto toBookingDto(final Booking item) {
        return BookingDto.builder()
                .id(item.getId())
                .itemId(item.getItem().getId())
                .bookerId(item.getBooker().getId())
                .start(item.getStart())
                .end(item.getEnd())
                .status(item.getStatus())
                .build();
    }

    private BookingMapper() {
    }
}