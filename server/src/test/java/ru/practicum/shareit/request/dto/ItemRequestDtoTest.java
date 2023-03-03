package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void serializeInCorrectFormat() throws IOException {
        ItemRequestDto receipt = new ItemRequestDto(
                1,
                "test",
                1,
                LocalDateTime.of(2021, 1, 1, 1, 1,1),
                null);

        JsonContent<ItemRequestDto> json = jacksonTester.write(receipt);

        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2021-01-01T01:01:01.000");
    }
}
