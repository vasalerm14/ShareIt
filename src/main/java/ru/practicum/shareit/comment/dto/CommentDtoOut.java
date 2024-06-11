package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoOut {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    @JsonCreator
    public CommentDtoOut(
            @JsonProperty("id") int id,
            @JsonProperty("text") String text,
            @JsonProperty("authorName") String authorName,
            @JsonProperty("created") LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
