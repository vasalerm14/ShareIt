package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> saveNewRequest(@Validated @RequestBody ItemRequestDtoRequest requestDto,
                                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("POST / requests {} / user {}", requestDto.getDescription(), userId);
        return requestClient.saveNewRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / requests / requestor {}", userId);
        return requestClient.getRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / requests");
        return requestClient.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable int requestId,
                                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET / request {} / user {}", requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}
