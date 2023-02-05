package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.utils.GeneratorId;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("inMemory")
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items;
    private final Map<Integer, Set<Integer>> itemsByUser;
    private final GeneratorId generatorId;

    public InMemoryItemStorage(GeneratorId generatorId) {
        this.items = new HashMap<>();
        this.itemsByUser = new HashMap<>();
        this.generatorId = generatorId;
    }

    @Override
    public Item add(Item item) {
        Item creatingUser = item.toBuilder().id(generatorId.getId()).build();
        items.put(creatingUser.getId(), creatingUser);
        if (!itemsByUser.containsKey(creatingUser.getOwner())) {
            itemsByUser.put(creatingUser.getOwner(), new HashSet<>());
        }
        itemsByUser.get(creatingUser.getOwner()).add(creatingUser.getId());
        return creatingUser;
    }

    @Override
    public Item remove(Item item) {
        itemsByUser.get(item.getOwner()).remove(item.getId());
        return items.remove(item.getId());
    }

    @Override
    public Item remove(int itemId) {
        if (items.containsKey(itemId)) {
            itemsByUser.get(items.get(itemId).getOwner()).remove(itemId);
        }
        return items.remove(itemId);
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public List<Item> getAllByUser(int userId) {
        if (!itemsByUser.containsKey(userId)) {
            return Collections.emptyList();
        }

        return itemsByUser.get(userId).stream()
                .map(items::get)
                .collect(Collectors.toList());
    }

    @Override
    public Item get(int itemId) {
        return items.get(itemId);
    }

    @Override
    public boolean contains(int itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        final var russianLocal = new Locale("ru");
        final var tempText = text.toLowerCase(russianLocal);
        return items.values()
                .stream()
                .filter(Item::getIsAvailable)
                .filter(item -> item.getDescription().toLowerCase(russianLocal).contains(tempText)
                        || item.getName().toLowerCase(russianLocal).contains(tempText))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> addComment(Comment comment) {
        return null;
    }
}
