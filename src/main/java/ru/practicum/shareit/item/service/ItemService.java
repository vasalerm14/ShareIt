package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(int itemId);

    List<ItemDto> getItemsByOwner(int userId);

    List<ItemDto> getItemBySearch(String text);

    ItemDto saveNewItem(ItemDto itemDto, int userId);

    ItemDto updateItem(int itemId, ItemDto itemDto, int userId);
}