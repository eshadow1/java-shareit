package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	private long id;

	@FutureOrPresent
	private LocalDateTime start;

	@Future
	private LocalDateTime end;

	private Integer itemId;

	private Integer bookerId;

	private BookingState status;
}
