package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.utils.GeneratorId;

import java.util.*;

@Repository
@Qualifier("inMemory")
public class InMemoryBookingStorage implements BookingStorage {
    private final Map<Integer, Booking> bookings;
    private final GeneratorId generatorId;

    public InMemoryBookingStorage(GeneratorId generatorId) {
        this.bookings = new HashMap<>();
        this.generatorId = generatorId;
    }

    @Override
    public Booking add(Booking booking) {
        Booking creatingBooking = booking.toBuilder().id(generatorId.getId()).build();
        return bookings.put(creatingBooking.getId(), creatingBooking);
    }

    @Override
    public Booking remove(Booking user) {
        return bookings.remove(user.getId());
    }

    @Override
    public Booking remove(int userId) {
        return bookings.remove(userId);
    }

    @Override
    public Booking update(Booking user) {
        return bookings.put(user.getId(), user);
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Booking get(int idUser) {
        return bookings.get(idUser);
    }

    @Override
    public boolean contains(int userId) {
        return bookings.containsKey(userId);
    }
}
