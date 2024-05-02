package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item getItemById(int itemId);

    List<Item> getItemsByOwner(int userId);

    List<Item> getItemBySearch(String text);

    Item saveNewItem(Item item, int userId);
}