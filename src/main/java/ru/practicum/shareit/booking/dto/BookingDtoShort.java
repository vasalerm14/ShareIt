package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoShort {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private int bookerId;
}