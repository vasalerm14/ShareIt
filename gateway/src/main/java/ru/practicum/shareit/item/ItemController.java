package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> saveNewItem(@Valid @RequestBody ItemDtoRequest itemDto,
                                              @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / items {} / user {}", itemDto.getName(), userId);
        return itemClient.saveNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable int itemId,
                                             @RequestBody ItemDtoRequest itemDto,
                                             @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("PATCH / items {} / user {}", itemId, userId);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable int itemId,
                                              @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / items {} / user {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / items / user {}", userId);
        return itemClient.getItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam(defaultValue = "1") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam String text,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / search / {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return itemClient.getItemBySearch(from, size, text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveNewComment(@PathVariable int itemId,
                                                 @Valid @RequestBody CommentDtoRequest commentDto,
                                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / comment / item {}", itemId);
        return itemClient.saveNewComment(itemId, commentDto, userId);
    }
}