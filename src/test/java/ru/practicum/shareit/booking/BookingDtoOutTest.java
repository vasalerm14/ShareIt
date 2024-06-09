package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.dto.UserDtoShort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoOutTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        UserDtoShort booker = new UserDtoShort(1, "John Doe");
        ItemDtoShort item = new ItemDtoShort(1, "Laptop");
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 12, 0);
        BookingDtoOut bookingDtoOut = new BookingDtoOut(
                1,
                start,
                end,
                item,
                booker,
                BookingStatus.APPROVED
        );

        String json = objectMapper.writeValueAsString(bookingDtoOut);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"start\":\"2024-06-10T09:00:00\"");
        assertThat(json).contains("\"end\":\"2024-06-10T12:00:00\"");
        assertThat(json).contains("\"item\":{\"id\":1,\"name\":\"Laptop\"}");
        assertThat(json).contains("\"booker\":{\"id\":1,\"name\":\"John Doe\"}");
        assertThat(json).contains("\"status\":\"APPROVED\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{"
                + "\"id\":1,"
                + "\"start\":\"2024-06-10T09:00:00\","
                + "\"end\":\"2024-06-10T12:00:00\","
                + "\"item\":{\"id\":1,\"name\":\"Laptop\"},"
                + "\"booker\":{\"id\":1,\"name\":\"John Doe\"},"
                + "\"status\":\"APPROVED\""
                + "}";

        BookingDtoOut bookingDtoOut = objectMapper.readValue(json, BookingDtoOut.class);

        assertThat(bookingDtoOut.getId()).isEqualTo(1);
        assertThat(bookingDtoOut.getStart()).isEqualTo(LocalDateTime.of(2024, 6, 10, 9, 0));
        assertThat(bookingDtoOut.getEnd()).isEqualTo(LocalDateTime.of(2024, 6, 10, 12, 0));
        assertThat(bookingDtoOut.getItem().getId()).isEqualTo(1);
        assertThat(bookingDtoOut.getItem().getName()).isEqualTo("Laptop");
        assertThat(bookingDtoOut.getBooker().getId()).isEqualTo(1);
        assertThat(bookingDtoOut.getBooker().getName()).isEqualTo("John Doe");
        assertThat(bookingDtoOut.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
