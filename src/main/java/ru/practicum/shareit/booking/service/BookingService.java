package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, int userId);

    BookingDtoOut approve(int bookingId, Boolean isApproved, int userId);

    BookingDtoOut getBookingById(int bookingId, int userId);

    List<BookingDtoOut> getAllByBooker(String subState, int bookerId);

    List<BookingDtoOut> getAllByOwner(int ownerId, String state);
}
