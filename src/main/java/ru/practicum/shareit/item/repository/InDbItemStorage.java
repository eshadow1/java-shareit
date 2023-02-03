package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
@Qualifier("inDb")
public class InDbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public InDbItemStorage(@Lazy ItemRepository itemRepository,
                           @Lazy CommentRepository commentRepository) {

        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Item> getAllByUser(int userId) {
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> {
                    var lastAll = itemRepository.findAllLastBookingByItemId(item.getId());
                    BookingItemDao last = lastAll.isEmpty() ? null : lastAll.get(0);

                    var futureAll = itemRepository.findAllFutureBookingByItemId(item.getId());
                    BookingItemDao future = futureAll.isEmpty() ? null : futureAll.get(0);

                    return item.toBuilder()
                            .comments(commentRepository.findAllByItemId(item.getId()))
                            .lastBooking(last)
                            .nextBooking(future)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Item get(int itemId) {
        var item = itemRepository.findById(itemId).orElse(null);

        var lastAll = itemRepository.findAllLastBookingByItemId(item.getId());
        BookingItemDao last = lastAll.isEmpty() ? null : lastAll.get(0);

        var futureAll = itemRepository.findAllFutureBookingByItemId(itemId);
        BookingItemDao future = futureAll.isEmpty() ? null : futureAll.get(0);

        return item.toBuilder()
                .comments(commentRepository.findAllByItemId(itemId))
                .lastBooking(last)
                .nextBooking(future)
                .build();
    }

    @Override
    public Item add(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item remove(Item item) {
        itemRepository.delete(item);
        return item;
    }

    @Override
    public Item remove(int itemId) {
        var item = get(itemId);
        itemRepository.deleteById(itemId);
        return item;
    }

    @Override
    public Item update(Item item) {
        itemRepository.update(item.getId(), item.getName(), item.getDescription(), item.getIsAvailable());
        return get(item.getId());
    }

    @Override
    public boolean contains(int itemId) {
        return itemRepository.findById(itemId).isPresent();
    }

    @Override
    public List<Item> searchItems(String text) {
        final var russianLocal = new Locale("ru");
        final var tempText = "%" + text.toLowerCase(russianLocal) + "%";

        return itemRepository.searchItemsBy(tempText);
    }

    @Override
    public Comment addComment(Comment comment) {
        var items = commentRepository.getBookingByItemIdAndAuthorId(comment.getItemId(), comment.getAuthorId());
        if (items.isEmpty()) {
            return null;
        }
        return commentRepository.save(comment);
    }
}
