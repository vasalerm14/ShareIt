package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDtoIn {
    @Size(max = 1000)
    @NotBlank
    private String text;
}
