package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut saveNewBooking(@Validated @RequestBody BookingDtoIn bookingDtoIn,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / bookings");
        return bookingService.saveNewBooking(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approve(@PathVariable int bookingId, @RequestParam(name = "approved") Boolean isApproved,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("PATCH / bookings / {}", bookingId);
        return bookingService.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / bookings / {}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBooker(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestHeader("X-Sharer-User-Id") int bookerId) {
        log.info("GET / ByBooker {}", bookerId);
        return bookingService.getAllByBooker(from, size, state, bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size,
                                             @RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("GET / ByOwner / {}", ownerId);
        return bookingService.getAllByOwner(from, size, state, ownerId);
    }
}