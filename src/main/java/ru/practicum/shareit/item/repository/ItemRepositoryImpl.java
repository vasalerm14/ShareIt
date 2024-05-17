package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


@Component
public class ItemRepositoryImpl implements ItemRepository {
    private static int generatorId = 0;
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> userItemIndex = new HashMap<>();

    @Override
    public Item getItemById(int itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new EntityNotFoundException("Объект не найден");
        }
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(int userId) {
        return userItemIndex.get(userId);
    }

    @Override
    public List<Item> getItemBySearch(String text) {
        List<Item> findItems = new ArrayList<>();
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()) {
                findItems.add(item);
            }
        }
        return findItems;
    }

    @Override
    public Item saveNewItem(Item item, int userId) {
        item.setId(++generatorId);
        item.setOwnerId(userId);
        items.put(item.getId(), item);
        List<Item> itemsByOwner = userItemIndex.get(item.getOwnerId());
        if (itemsByOwner == null) {
            itemsByOwner = new ArrayList<>();
            itemsByOwner.add(item);
            userItemIndex.put(item.getOwnerId(), itemsByOwner);
            return item;
        }
        itemsByOwner.add(item);
        return item;
    }
}
