package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingServiceImpl;

    public BookingController(BookingService bookingServiceImpl) {
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDao addBooking(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                 @Valid @RequestBody BookingDto booking) {
        log.info("Получен запрос на добавление заказа: " + booking);

        return bookingServiceImpl.addBooking(BookingMapper.fromBookingDto(booking, userId));
    }

    @PatchMapping("/{id}")
    public BookingDao updateBooking(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                    @PathVariable int id,
                                    @RequestParam boolean approved) {
        log.info("Получен запрос на обновление заказа " + id + " от пользователя " + userId);

        return bookingServiceImpl.updateBooking(id, userId, approved);
    }

    @GetMapping("/{id}")
    public BookingDao getBooking(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                 @PathVariable int id) {
        log.info("Получен запрос на получение заказа " + id);

        return bookingServiceImpl.getBooking(id, userId);
    }

    @GetMapping
    public List<BookingDao> getBookings(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение всех заказов от пользователя " + userId);

        try {
            var newState = State.valueOf(state);

            return bookingServiceImpl.getAllBookingUser(userId, newState);
        } catch (IllegalArgumentException er) {
            throw new IllegalStateException("Unknown state: " + state);
        }
    }

    @GetMapping("/owner")
    public List<BookingDao> getBookingsOwner(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение всех заказов от пользователя " + userId);
        try {
            var newState = State.valueOf(state);

            return bookingServiceImpl.getAllBookingOwner(userId, newState);
        } catch (IllegalArgumentException er) {
            throw new IllegalStateException("Unknown state: " + state);
        }
    }

    @DeleteMapping("/{id}")
    public BookingDao removeBooking(@PathVariable int id) {
        log.info("Получен запрос на удаление заказа " + id);

        return bookingServiceImpl.removeBooking(id);
    }
}
