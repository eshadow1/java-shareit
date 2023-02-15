package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDao;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(UserRepository userRepository,
                           ItemRepository itemRepository,
                           CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    public Item addItem(Item item) {
        CheckerItem.checkedUserContains(userRepository, item.getOwner().getId());

        return itemRepository.save(item);
    }

    public Item updateItem(int id, Item item) {
        CheckerItem.checkedItemContains(itemRepository, id);
        CheckerItem.checkedUserContains(userRepository, item.getOwner().getId());

        var oldItem = get(id);
        CheckerItem.checkedItemForUser(oldItem.getOwner().getId(), item.getOwner().getId());

        oldItem = ItemMapper.update(oldItem, item);

        var tempItem = itemRepository.findById(oldItem.getId()).orElseThrow();

        tempItem.setName(oldItem.getName());
        tempItem.setDescription(oldItem.getDescription());
        tempItem.setIsAvailable(oldItem.getIsAvailable());

        return itemRepository.save(tempItem);
    }

    public Item getItem(int itemId) {
        CheckerItem.checkedItemContains(itemRepository, itemId);

        return get(itemId);
    }

    public List<Item> getAllItemsByUser(int userId, int from, int size) {
        CheckerItem.checkedUserContains(userRepository, userId);

        return getAllByUser(userId, from, size);
    }

    public Item removeItem(int itemId) {
        var item = get(itemId);
        itemRepository.deleteById(itemId);
        return item;
    }

    @Override
    public List<Item> searchItems(int userId, String text, int from, int size) {
        CheckerItem.checkedUserContains(userRepository, userId);

        final var russianLocal = new Locale("ru");
        final var tempText = "%" + text.toLowerCase(russianLocal) + "%";

        return itemRepository.searchItemsBy(tempText,
                PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"))).getContent();
    }

    @Override
    public Comment addComment(Comment comment) {
        CheckerItem.checkedUserContains(userRepository, comment.getAuthor().getId());
        CheckerItem.checkedItemContains(itemRepository, comment.getItem().getId());
        var addComment = addCommentOrEmpty(comment);
        CheckerItem.checkedAddComment(addComment, comment.getItem().getId());
        return addComment.get();
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
    public List<Item> getAllByUser(int userId) {
        var user = itemRepository.getUser(userId);
        if (user.isEmpty())
            return null;
        return itemRepository.findByOwnerOrderByIdAsc(user.get()).stream()
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
    public boolean contains(Integer itemId) {
        return itemRepository.findById(itemId).isPresent();
    }

    public List<Item> getAllByUser(int userId, int from, int size) {
        var user = itemRepository.getUser(userId);
        if (user.isEmpty())
            return null;
        return itemRepository.findByOwner(user.get(),
                        PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"))).stream()
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

    private Optional<Comment> addCommentOrEmpty(Comment comment) {
        var items = commentRepository.getBookingByItemIdAndAuthorId(comment.getItem().getId(), comment.getAuthor().getId());
        if (items.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(commentRepository.save(comment));
    }
}

