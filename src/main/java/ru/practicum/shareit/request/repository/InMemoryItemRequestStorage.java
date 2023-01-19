package ru.practicum.shareit.request.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.utils.GeneratorId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Qualifier("inMemory")
public class InMemoryItemRequestStorage implements ItemRequestStorage {
    private final Map<Integer, ItemRequest> bookings;
    private final GeneratorId generatorId;

    public InMemoryItemRequestStorage(GeneratorId generatorId) {
        this.bookings = new HashMap<>();
        this.generatorId = generatorId;
    }

    @Override
    public ItemRequest add(ItemRequest booking) {
        ItemRequest creatingBooking = booking.toBuilder().id(generatorId.getId()).build();
        return bookings.put(creatingBooking.getId(), creatingBooking);
    }

    @Override
    public ItemRequest remove(ItemRequest user) {
        return bookings.remove(user.getId());
    }

    @Override
    public ItemRequest remove(int userId) {
        return bookings.remove(userId);
    }

    @Override
    public ItemRequest update(ItemRequest user) {
        return bookings.put(user.getId(), user);
    }

    @Override
    public List<ItemRequest> getAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public ItemRequest get(int idUser) {
        return bookings.get(idUser);
    }

    @Override
    public boolean contains(int userId) {
        return bookings.containsKey(userId);
    }
}
