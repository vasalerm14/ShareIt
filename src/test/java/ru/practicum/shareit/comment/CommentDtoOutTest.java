package ru.practicum.shareit.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.comment.dto.CommentDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoOutTest {

    @Autowired
    private JacksonTester<CommentDtoOut> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentDtoOut commentDtoOut = new CommentDtoOut(1, "text", "author", now);

        JsonContent<CommentDtoOut> result = json.write(commentDtoOut);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).hasJsonPathStringValue("$.authorName");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    void testDeserialize() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String content = "{\"id\":1,\"text\":\"text\",\"authorName\":\"author\",\"created\":\"" + now + "\"}";

        CommentDtoOut result = objectMapper.readValue(content, CommentDtoOut.class);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getText()).isEqualTo("text");
        assertThat(result.getAuthorName()).isEqualTo("author");
        assertThat(result.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo(now);
    }
}
