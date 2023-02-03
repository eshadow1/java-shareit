package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "booker_id")
    private Integer bookerId;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
