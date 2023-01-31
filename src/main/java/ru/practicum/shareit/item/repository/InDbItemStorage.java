package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Locale;

@Repository
@Qualifier("inDb")
public class InDbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;

    public InDbItemStorage(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllByUser(int userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public Item get(int itemId) {
        return itemRepository.findById(itemId).orElse(null);
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
        return  itemRepository.findById(itemId).isPresent();
    }

    @Override
    public List<Item> searchItems(String text) {
        final var russianLocal = new Locale("ru");
        final var tempText =  "%" + text.toLowerCase(russianLocal) + "%";

        return itemRepository.searchItemsBy(tempText);
    }
}
