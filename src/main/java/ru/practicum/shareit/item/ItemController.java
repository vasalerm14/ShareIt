package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDtoOut saveNewItem(@Valid @RequestBody ItemDtoIn itemDtoIn,
                                  @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.saveNewItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@PathVariable int itemId,
                                 @RequestBody ItemDtoIn itemDtoIn,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.updateItem(itemId, itemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getFilmBySearch(@RequestParam String text) {
        return itemService.getItemBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@PathVariable int itemId,
                                    @Valid @RequestBody CommentDtoIn commentDtoIn,
                                    @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.saveNewComment(itemId, commentDtoIn, userId);
    }
}