package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDtoIn {
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotNull
    private Boolean available;

    private Integer requestId;
}