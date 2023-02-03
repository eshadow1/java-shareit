package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.item.ItemDao;
import ru.practicum.shareit.user.dto.UserDao;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookingDao {
    private int id;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;

    @OneToOne(fetch = FetchType.LAZY)
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "item_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "id")),
            @AttributeOverride(name = "name", column = @Column(name = "name"))
    })
    private ItemDao item;

    @OneToOne(fetch = FetchType.LAZY)
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "booker_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "id")),
    })
    private UserDao booker;

    private Status status;
}