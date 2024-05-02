package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto getItemById(int itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new EntityNotFoundException("Объект не найден");
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(int userId) {
        userService.getUserById(userId);
        List<ItemDto> allItems = new ArrayList<>();
        for (Item item : itemRepository.getItemsByOwner(userId)) {
            allItems.add(ItemMapper.toItemDto(item));
            ;
        }
        return allItems;
    }

    @Override
    public List<ItemDto> getItemBySearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<ItemDto> result = new ArrayList<>();
        for (Item item : itemRepository.getItemBySearch(text)) {
            result.add(ItemMapper.toItemDto(item));
        }
        return result;
    }

    @Override
    public ItemDto saveNewItem(ItemDto itemDto, int userId) {
        userService.getUserById(userId);
        return ItemMapper.toItemDto(itemRepository.saveNewItem(ItemMapper.toItem(itemDto), userId));
    }

    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int userId) {
        userService.getUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new EntityNotFoundException("Объект не найден");
        }
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (item.getOwnerId() == userId) {
            if (name != null && !name.isBlank()) {
                item.setName(name);
            }
            if (description != null && !description.isBlank()) {
                item.setDescription(description);
            }
            if (available != null) {
                item.setAvailable(available);
            }
        } else {
            throw new NotOwnerException("Пользователь не является собственником");
        }
        return ItemMapper.toItemDto(item);
    }
}