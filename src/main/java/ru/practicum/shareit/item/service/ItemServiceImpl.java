package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.MappingItem;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.UserNotFoundException;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(@Qualifier("inMemory") ItemStorage itemStorage,
                           @Qualifier("inMemory") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public Item addItem(Item item) {
        checkedUserContains(item.getOwner());

        return itemStorage.add(item);
    }

    public Item updateItem(int id, Item item) {
        checkedItemContains(id);
        checkedUserContains(item.getOwner());

        var oldItem = itemStorage.get(id);
        checkedItemForUser(oldItem.getOwner(), item.getOwner());

        oldItem = MappingItem.update(oldItem, item);
        itemStorage.update(oldItem);
        return oldItem;
    }

    public Item getItem(int itemId) {
        checkedItemContains(itemId);

        return itemStorage.get(itemId);
    }

    public List<Item> getAllItemsByUser(int userId) {
        checkedUserContains(userId);

        return itemStorage.getAllByUser(userId);
    }

    public Item removeItem(int itemId) {
        return itemStorage.remove(itemId);
    }

    @Override
    public List<Item> searchItems(int userId, String text) {
        checkedUserContains(userId);

        return itemStorage.searchItems(text.toLowerCase());
    }

    private void checkedItemContains(int id) {
        if (!itemStorage.contains(id)) {
            throw new ContainsFalseException("Предмет с id " + id + " не найден");
        }
    }

    private void checkedItemForUser(int id, int owner) {
        if (id != owner) {
            throw new ContainsFalseException("Предмет с id " + id + " не принадлежит пользователю " + owner);
        }
    }

    private void checkedUserContains(int owner) {
        if (!userStorage.contains(owner)) {
            throw new UserNotFoundException("Пользователь с id " + owner + " не найден");
        }
    }
}

