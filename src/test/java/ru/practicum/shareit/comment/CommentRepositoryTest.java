package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User(null, "user", "user@mail.ru");
        user = userRepository.save(user);

        item = new Item(null, "item", "cool", true, user, null);
        item = itemRepository.save(item);

        comment = new Comment(null, "abc", item, user, LocalDateTime.of(2023, 7, 1, 12, 12, 12));
        comment = commentRepository.save(comment);
    }

    @Test
    @DirtiesContext
    void findAllByItemId() {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments.get(0).getId(), notNullValue());
        assertThat(comments.get(0).getText(), equalTo(comment.getText()));
        assertThat(comments.size(), equalTo(1));
    }
}
