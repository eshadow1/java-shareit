package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingTest {
    @Autowired
    private JacksonTester<Booking> jacksonTester;

    @Test
    void serializeInCorrectFormat() throws IOException {
        Booking receipt = new Booking(
                1,
                LocalDateTime.of(2021, 1, 1, 11, 0, 1),
                LocalDateTime.of(2021, 1, 1, 11, 1, 1),
                null,
                null,
                Status.REJECTED);

        JsonContent<Booking> json = jacksonTester.write(receipt);

        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2021-01-01T11:00:01");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2021-01-01T11:01:01");
    }
}
