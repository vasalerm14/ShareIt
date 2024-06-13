package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequestDtoIn;
import ru.practicum.shareit.request.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDtoIn requestDtoIn) {
        return new ItemRequest(
                requestDtoIn.getDescription()
        );
    }

    public ItemRequestDtoOut toItemRequestDtoOut(ItemRequest request) {
        return new ItemRequestDtoOut(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }
}