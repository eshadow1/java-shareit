package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> addBooking(@Valid @RequestBody Booking booking) {
        log.info("Получен запрос на добавление заказа: " + booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addBooking(booking));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable int id, @RequestBody Booking booking) {
        log.info("Получен запрос на обновление заказа " + id + ": " + booking);

        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(id, booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable int id) {
        log.info("Получен запрос на получение заказа " + id);

        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBooking(id));
    }

    @GetMapping
    public List<Booking> getBookings() {
        log.info("Получен запрос на получение всех заказов");
        return bookingService.getAllBooking();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> removeUser(@PathVariable int id) {
        log.info("Получен запрос на удаление заказа " + id);

        return ResponseEntity.status(HttpStatus.OK).body(bookingService.removeBooking(id));
    }
}
