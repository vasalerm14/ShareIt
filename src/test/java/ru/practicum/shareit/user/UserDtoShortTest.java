package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoShort;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoShortTest {

    @Autowired
    private JacksonTester<UserDtoShort> json;

    @Test
    void testSerialize() throws Exception {
        UserDtoShort userDtoShort = new UserDtoShort(1, "name");

        JsonContent<UserDtoShort> result = json.write(userDtoShort);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"name\"}";

        UserDtoShort result = json.parseObject(content);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("name");
    }
}
