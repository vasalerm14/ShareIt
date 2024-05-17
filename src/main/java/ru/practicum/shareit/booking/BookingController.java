package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut saveNewBooking(@Validated @RequestBody BookingDtoIn bookingDtoIn,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.saveNewBooking(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approve(@PathVariable int bookingId, @RequestParam(name = "approved") Boolean isApproved,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.getAllByBooker(state, bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return bookingService.getAllByOwner(ownerId, state);
    }
}
