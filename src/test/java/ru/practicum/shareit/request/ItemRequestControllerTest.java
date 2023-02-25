package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    private static String endpoint;

    private static String jsonCorrectItemRequest;

    private static String jsonIncorrectItemRequest;

    private static ItemRequest itemRequest;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private UserService userService;

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

        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("test")
                .requestorId(user)
                .created(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                .build();
    }

    @Test
    void addItemRequest() {
        when(itemRequestService.addItemRequest(any()))
                .thenReturn(itemRequest);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonCorrectItemRequest)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemRequestService, times(1)).addItemRequest(any());
    }

    @Test
    void getItemRequests() {
        when(itemRequestService.getAllItemRequestByUser(anyInt()))
                .thenReturn(Collections.singletonList(itemRequest));

        try {
            mockMvc.perform(get(endpoint)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemRequestService, times(1)).getAllItemRequestByUser(anyInt());
    }

    @Test
    void getItemRequest() {
        when(itemRequestService.getItemRequest(anyInt(), anyInt()))
                .thenReturn(itemRequest);

        try {
            mockMvc.perform(get(endpoint + "/1")
                            .content(jsonCorrectItemRequest)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemRequestService, times(1)).getItemRequest(anyInt(), anyInt());
    }

    @Test
    void getAllItemRequest() {
        when(itemRequestService.getItemRequestFromSize(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(itemRequest));

        try {
            mockMvc.perform(get(endpoint + "/all")
                            .content(jsonCorrectItemRequest)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemRequestService, times(1)).getItemRequestFromSize(anyInt(), anyInt(), anyInt());
    }
}