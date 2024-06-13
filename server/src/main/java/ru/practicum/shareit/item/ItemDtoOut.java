package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.user.UserDtoShort;

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

    @JsonCreator
    public ItemDtoOut(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("available") Boolean available,
            @JsonProperty("owner") UserDtoShort owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}
