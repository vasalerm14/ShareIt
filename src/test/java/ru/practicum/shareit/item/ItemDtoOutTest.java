package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDtoShort;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoOutTest {

    @Autowired
    private JacksonTester<ItemDtoOut> json;

    @Test
    void testSerialize() throws Exception {
        UserDtoShort owner = new UserDtoShort(1, "owner");
        ItemDtoOut itemDtoOut = new ItemDtoOut(1, "itemName", "itemDescription", true, owner);

        JsonContent<ItemDtoOut> result = json.write(itemDtoOut);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).hasJsonPathMapValue("$.owner");
        assertThat(result).extractingJsonPathMapValue("$.owner").containsEntry("id", 1).containsEntry("name", "owner");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"itemName\",\"description\":\"itemDescription\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"owner\"}}";

        ItemDtoOut result = json.parseObject(content);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("itemName");
        assertThat(result.getDescription()).isEqualTo("itemDescription");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getOwner().getId()).isEqualTo(1);
        assertThat(result.getOwner().getName()).isEqualTo("owner");
    }
}