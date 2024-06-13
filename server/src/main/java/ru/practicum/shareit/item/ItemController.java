package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDtoOut saveNewItem(@RequestBody ItemDtoIn itemDtoIn,
                                  @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / items {} / user {}", itemDtoIn.getName(), userId);
        return itemService.saveNewItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@PathVariable int itemId,
                                 @RequestBody ItemDtoIn itemDtoIn,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("PATCH / items {} / user {}", itemId, userId);
        return itemService.updateItem(itemId, itemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / items {} / user {}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsByOwner(@RequestParam(defaultValue = "1") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / items / user {}", userId);
        return itemService.getItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getFilmBySearch(@RequestParam(defaultValue = "1") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam String text) {
        log.info("GET / search / {}", text);
        return itemService.getItemBySearch(from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut saveNewComment(@PathVariable int itemId,
                                        @RequestBody CommentDtoIn commentDtoIn,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / comment / item {}", itemId);
        return itemService.saveNewComment(itemId, commentDtoIn, userId);
    }
}