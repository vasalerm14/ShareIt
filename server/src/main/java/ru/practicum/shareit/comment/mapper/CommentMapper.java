package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public Comment toComment(CommentDtoIn commentDtoIn, Item item, User author) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setText(commentDtoIn.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}