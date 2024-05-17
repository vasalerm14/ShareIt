package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemIsNotAvailableException extends RuntimeException {
    public ItemIsNotAvailableException(String message) {
        super(message);
    }
}
