package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.user.dto.UserDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoOut {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private List<CommentDtoOut> comments;
    private UserDtoShort owner;
    private Integer requestId;

    public ItemDtoOut(int id, String name, String description, Boolean available, UserDtoShort owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}