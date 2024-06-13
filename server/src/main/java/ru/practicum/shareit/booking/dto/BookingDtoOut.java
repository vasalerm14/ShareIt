package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.ItemDtoShort;
import ru.practicum.shareit.user.UserDtoShort;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoOut {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoShort item;
    private UserDtoShort booker;
    private BookingStatus status;
}
