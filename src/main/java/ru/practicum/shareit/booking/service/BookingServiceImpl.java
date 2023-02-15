package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<BookingDao> getAllBookingUser(int userId, State state, int from, int size) {
        CheckerBooking.checkedUserContains(userRepository, userId);
        return getAllByUserAndState(userId, state, from, size);
    }

    @Override
    public List<BookingDao> getAllBookingOwner(int userId, State state, int from, int size) {
        CheckerBooking.checkedUserContains(userRepository, userId);
        var items = itemService.getAllByUser(userId);
        return getAllByItems(items, state, from, size);
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

    private List<BookingDao> getAllByItems(List<Item> items, State state, int from, int size) {
        var allItem = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> allBooking;
        var time = LocalDateTime.now();
        var pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));

        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByItemIdIn(allItem, pageRequest).getContent();
                break;
            case WAITING:
                allBooking = bookingRepository.findByItemIdInAndStatus(allItem, Status.WAITING, pageRequest).getContent();
                break;
            case REJECTED:
                allBooking = bookingRepository.findByItemIdInAndStatus(allItem, Status.REJECTED, pageRequest).getContent();
                break;
            case PAST:
                allBooking = bookingRepository.findByItemIdInAndEndBefore(allItem, time, pageRequest).getContent();
                break;
            case FUTURE:
                allBooking = bookingRepository.findByItemIdInAndStartAfter(allItem, time, pageRequest).getContent();
                break;
            case CURRENT:
                allBooking = bookingRepository.findByItemIdInAndStartBeforeAndEndAfter(allItem, time, time, pageRequest).getContent();
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

        return allBooking.stream()
                .map(booking -> BookingMapper.toBookingDao(booking,
                        bookingRepository.getItem(booking.getItemId()).orElse(null)))
                .collect(Collectors.toList());
    }

    private List<BookingDao> getAllByUserAndState(int userId, State state, int from, int size) {
        List<Booking> allBooking;
        var pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));

        switch (state) {
            case ALL:
                allBooking = bookingRepository.findByBookerId(userId, pageRequest).getContent();
                break;
            case WAITING:
                allBooking = bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING, pageRequest).getContent();
                break;
            case REJECTED:
                allBooking = bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED, pageRequest).getContent();
                break;
            case PAST:
                allBooking = bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now(), pageRequest).getContent();
                break;
            case FUTURE:
                allBooking = bookingRepository.findByBookerIdAndStartAfter(userId, LocalDateTime.now(), pageRequest).getContent();
                break;
            case CURRENT:
                var time = LocalDateTime.now();
                allBooking = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, time, time, pageRequest).getContent();
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
