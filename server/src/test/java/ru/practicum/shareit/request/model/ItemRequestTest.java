package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestTest {
    @Autowired
    private JacksonTester<ItemRequest> jacksonTester;

    @Test
    void serializeInCorrectFormat() throws IOException {
        ItemRequest receipt = new ItemRequest(
                1,
                "test",
                null,
                LocalDateTime.of(2021, 1, 1, 1, 1,1),
                Collections.emptyList());

        JsonContent<ItemRequest> json = jacksonTester.write(receipt);

        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2021-01-01T01:01:01.000");
    }
}
