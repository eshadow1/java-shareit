package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ItemRequestControllerIntegralTest {

    private static String endpoint;

    private static String jsonCorrectItemRequest;

    private static String jsonIncorrectItemRequest;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        endpoint = "/requests";

        jsonCorrectItemRequest = "{" +
                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"" +
                "}";
        jsonIncorrectItemRequest = "{" +
                "    \"description\": null" +
                "}";
    }

    @BeforeEach
    public void beforeEach() {
        String jsonUser1 = "{" +
                "    \"name\": \"user\"," +
                "    \"email\": \"user@user.com\"" +
                "}";

        String jsonUser2 = "{" +
                "    \"name\": \"user2\"," +
                "    \"email\": \"user2@user.com\"" +
                "}";
        String endpointUser = "/users";

        try {
            mockMvc.perform(post(endpointUser)
                    .content(jsonUser1)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());
            mockMvc.perform(post(endpointUser)
                    .content(jsonUser2)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addItemRequest() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addBadItemRequest() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonIncorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addItemRequestWithoutUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addItemRequestWithNotFoundUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 99)
            ).andExpect(status().is4xxClientError());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getItemRequestsCorrectUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());

            mockMvc.perform(get(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getItemRequestsBadUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());

            mockMvc.perform(get(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 2)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getItemRequestWithCorrectUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());

            mockMvc.perform(get(endpoint + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getItemRequestWithOtherUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());

            mockMvc.perform(get(endpoint + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 2)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllItemRequest() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectItemRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
            ).andExpect(status().isOk());

            mockMvc.perform(get(endpoint + "/all")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 2)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}