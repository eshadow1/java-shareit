package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    void serializeInCorrectFormat() throws IOException {
        BookingDto receipt = new BookingDto(
                LocalDateTime.of(2021, 5, 9, 16, 0),
                LocalDateTime.of(2021, 5, 9, 16, 1),
                1);

        JsonContent<BookingDto> json = jacksonTester.write(receipt);

        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2021-05-09T16:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2021-05-09T16:01:00");
    }
}