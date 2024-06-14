package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDtoOut {
    private int id;
    private String description;
    private int requestorId;
    private LocalDateTime created;
    private List<ItemDtoOut> items;

    @JsonCreator
    public ItemRequestDtoOut(
            @JsonProperty("id") int id,
            @JsonProperty("description") String description,
            @JsonProperty("requestorId") int requestorId,
            @JsonProperty("created") LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}
