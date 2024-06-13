package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemDtoIn;
import ru.practicum.shareit.item.ItemDtoOut;
import ru.practicum.shareit.item.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@UtilityClass
public class ItemMapper {
    public ItemDtoOut toItemDtoOut(Item item) {
        ItemDtoOut itemDtoOut = new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDtoShort(item.getOwner())
        );
        if (item.getRequest() != null) {
            itemDtoOut.setRequestId(item.getRequest().getId());
        }
        return itemDtoOut;
    }

    public ItemDtoShort toItemDtoShort(Item item) {
        return new ItemDtoShort(
                item.getId(),
                item.getName()
        );
    }

    public Item toItem(ItemDtoIn itemDtoIn) {
        return new Item(
                itemDtoIn.getName(),
                itemDtoIn.getDescription(),
                itemDtoIn.getAvailable()
        );
    }
}
