package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoRequest {

	private long id;

	@FutureOrPresent
	@NotNull
	private LocalDateTime start;

	@Future
	@NotNull
	private LocalDateTime end;

	@NotNull
	private Long itemId;
}
