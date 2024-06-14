package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> saveNewBooking(@Validated @RequestBody BookItemRequestDto bookingDto,
												 @RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("POST / bookings");
		return bookingClient.saveNewBooking(bookingDto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@PathVariable int bookingId,
										  @RequestParam(name = "approved") Boolean isApproved,
										  @RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("PATCH / bookings / {}", bookingId);
		return bookingClient.approve(bookingId, isApproved, userId);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@PathVariable int bookingId,
												 @RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllByBooker(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
												 @RequestParam(defaultValue = "10") @Positive Integer size,
												 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
												 @RequestHeader("X-Sharer-User-Id") int bookerId) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("GET / ByBooker {}", bookerId);
		return bookingClient.getAllByBooker(from, size, state, bookerId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
												@RequestParam(defaultValue = "10") @Positive Integer size,
												@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
												@RequestHeader("X-Sharer-User-Id") int ownerId) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("GET / ByOwner / {}", ownerId);
		return bookingClient.getAllByOwner(from, size, state, ownerId);
	}
}