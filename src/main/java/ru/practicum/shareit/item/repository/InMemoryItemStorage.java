package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.GeneratorId;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("inMemory")
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items;
    private final GeneratorId generatorId;

    public InMemoryItemStorage(GeneratorId generatorId) {
        this.items = new HashMap<>();
        this.generatorId = generatorId;
    }

    @Override
    public Item add(Item item) {
        Item creatingUser = item.toBuilder().id(generatorId.getId()).build();
        items.put(creatingUser.getId(), creatingUser);
        return creatingUser;
    }

    @Override
    public Item remove(Item item) {
        return items.remove(item.getId());
    }

    @Override
    public Item remove(int userId) {
        return items.remove(userId);
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public List<Item> getAllByUser(int userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner() == userId)
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
        return items.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getDescription().toLowerCase().contains(text)
                        || item.getName().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
