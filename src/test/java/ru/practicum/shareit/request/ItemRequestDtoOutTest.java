package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoOutTest {

    @Autowired
    private JacksonTester<ItemRequestDtoOut> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(1, "description", 2, now, Collections.emptyList());

        JsonContent<ItemRequestDtoOut> result = json.write(itemRequestDtoOut);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).hasJsonPathNumberValue("$.requestorId");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).hasJsonPathArrayValue("$.items");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }

    @Test
    void testDeserialize() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String content = "{\"id\":1,\"description\":\"description\",\"requestorId\":2,\"created\":\"" + now + "\",\"items\":[]}";

        ItemRequestDtoOut result = objectMapper.readValue(content, ItemRequestDtoOut.class);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getRequestorId()).isEqualTo(2);
        assertThat(result.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo(now);
        assertThat(result.getItems()).isEmpty();
    }
}
