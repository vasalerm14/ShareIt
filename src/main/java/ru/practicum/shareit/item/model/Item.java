package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private Boolean available;
    @NotEmpty
    @NotBlank
    private int ownerId;

    public Item(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
