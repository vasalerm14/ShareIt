package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut getItemById(Integer itemId, int userId);

    List<ItemDtoOut> getItemsByOwner(int userId);

    List<ItemDtoOut> getItemBySearch(String text);

    ItemDtoOut saveNewItem(ItemDtoIn itemDtoIn, int userId);

    ItemDtoOut updateItem(int itemId, ItemDtoIn itemDtoIn, int userId);

    CommentDtoOut saveNewComment(int itemId, CommentDtoIn commentDtoIn, int userId);
}
