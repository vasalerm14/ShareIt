package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDtoIn {
    @Size(max = 255)
    private String name;
    @Size(max = 1000)
    private String description;
    private Boolean available;

    private Integer requestId;
}