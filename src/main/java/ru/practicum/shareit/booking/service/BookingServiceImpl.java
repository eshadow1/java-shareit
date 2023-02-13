package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDao;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;

    public BookingServiceImpl(UserRepository userRepository,
                              ItemService itemService,
                              BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }

    @Override
    public BookingDao addBooking(Booking booking) {
        CheckerBooking.checkedItemContains(itemService, booking.getItemId());
        CheckerBooking.checkItemAvailable(itemService, booking.getItemId());
        CheckerBooking.checkedUserContains(userRepository, booking.getBookerId());
        CheckerBooking.checkedTimeBooking(booking);
        var item = itemService.get(booking.getItemId());
        CheckerBooking.checkedBookerEqualsOwner(booking.getBookerId(), item.getOwner().getId());

        booking.setStatus(Status.WAITING);
        var tempBooking = bookingRepository.save(booking);
        return get(tempBooking.getId());
    }

    @Override
    public BookingDao updateBooking(int id, int userId, boolean approved) {
        CheckerBooking.checkedBookingContains(bookingRepository, id);
        CheckerBooking.checkedUserContains(userRepository, userId);

        var oldBooking = BookingMapper.fromBookingDao(get(id));
        CheckerBooking.checkedUpdate(oldBooking);


        var item = itemService.get(oldBooking.getItemId());
        CheckerBooking.checkedItemBookingByUser(item, userId);

        oldBooking = BookingMapper.update(oldBooking, approved);

        var tempBooking = bookingRepository.findById(oldBooking.getId()).orElseThrow();
        tempBooking.setStatus(oldBooking.getStatus());
        bookingRepository.save(tempBooking);

        return getBooking(id, userId);
    }

    @Override
    public BookingDao getBooking(int bookingId, int userId) {
        CheckerBooking.checkedBookingContains(bookingRepository, bookingId);
        CheckerBooking.checkedUserContains(userRepository, userId);

        var booking = get(bookingId);
        var item = itemService.get(booking.getItem().getId());

        CheckerBooking.checkedBookingContainsUsers(userId, booking, item);

        return booking;
    }

    @Override
    public List<BookingDao> getAllBookingUser(int userId, State state) {
        CheckerBooking.checkedUserContains(userRepository, userId);
        return getAllByUserAndState(userId, state);
    }

    @Override
    public List<BookingDao> getAllBookingOwner(int userId, State state) {
        CheckerBooking.checkedUserContains(userRepository, userId);
        var items = itemService.getAllByUser(userId);
        return getAllByItems(items, state);
    }

    @Override
    public BookingDao removeBooking(int bookingId) {
        var booking = get(bookingId);
        bookingRepository.deleteById(bookingId);
        return booking;
    }

    private BookingDao get(int bookingId) {
        var temp = bookingRepository.findById(bookingId).orElse(null);
        return temp != null ? BookingMapper.toBookingDao(temp,
                bookingRepository.getItem(temp.getItemId()).orElse(null)) : null;
    }

    private List<BookingDao> getAllByItems(List<Item> items, State state) {
        var allItem = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> allBooking;
        var time = LocalDateTime.now();
        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByItemIdInOrderByStartDesc(allItem);
                break;
            case WAITING:
                allBooking = bookingRepository.findByItemIdInAndStatusOrderByStartDesc(allItem, Status.WAITING);
                break;
            case REJECTED:
                allBooking = bookingRepository.findByItemIdInAndStatusOrderByStartDesc(allItem, Status.REJECTED);
                break;
            case PAST:
                allBooking = bookingRepository.findByItemIdInAndEndBeforeOrderByStartDesc(allItem, time);
                break;
            case FUTURE:
                allBooking = bookingRepository.findByItemIdInAndStartAfterOrderByStartDesc(allItem, time);
                break;
            case CURRENT:
                allBooking = bookingRepository.findByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(allItem, time, time);
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

        return allBooking.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }

    private List<BookingDao> getAllByUserAndState(int userId, State state) {
        List<Booking> allBooking;
        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case WAITING:
                allBooking = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                allBooking = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            case PAST:
                allBooking = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                allBooking = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case CURRENT:
                var time = LocalDateTime.now();
                allBooking = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time);
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

        return allBooking.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }
}
