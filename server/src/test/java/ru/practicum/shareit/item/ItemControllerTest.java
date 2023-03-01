package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exception.UserNotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static String endpoint;

    private static String jsonCorrectItem;

    private static String jsonComment;

    private static Item item;

    private static Item updateItem;

    private static User user;

    private static Comment comment;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        endpoint = "/items";

        user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        item = Item.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель")
                .isAvailable(true)
                .owner(user)
                .build();

        updateItem = Item.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель test")
                .isAvailable(true)
                .owner(user)
                .build();

        comment = Comment.builder()
                .text("test")
                .item(item)
                .author(user)
                .authorName(user.getName())
                .build();

        jsonCorrectItem = "{" +
                "    \"name\": \"Дрель\"," +
                "    \"description\": \"Простая дрель\"," +
                "    \"available\": true" +
                "}";

        jsonComment = "{" +
                "\"text\": \"Comment for item 1\"" +
                "}";
    }

    @Test
    void addItem() {
        when(itemService.addItem(any())).thenReturn(item);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonCorrectItem)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is(item.getName())))
                    .andExpect(jsonPath("$.description", is(item.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1)).addItem(any());
    }

    @Test
    void addItemComment() {
        when(userService.getUser(anyInt())).thenReturn(user);
        when(itemService.getItem(anyInt())).thenReturn(item);
        when(itemService.addComment(any())).thenReturn(comment);

        try {
            mockMvc.perform(post(endpoint + "/1/comment")
                            .content(jsonComment)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1)).addComment(any());
    }

    @Test
    void updateItem() {
        when(itemService.updateItem(anyInt(), any())).thenReturn(updateItem);

        try {
            mockMvc.perform(patch(endpoint + "/1")
                            .content(jsonCorrectItem)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(updateItem.getName())))
                    .andExpect(jsonPath("$.description", is(updateItem.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1)).updateItem(anyInt(), any());
    }

    @Test
    void getItem() {
        when(itemService.getItem(anyInt())).thenReturn(item);

        try {
            mockMvc.perform(get(endpoint + "/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(item.getName())))
                    .andExpect(jsonPath("$.description", is(item.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1)).getItem(anyInt());
    }

    @Test
    void getItems() {
        when(itemService.getAllItemsByUser(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(item));

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

        verify(itemService, times(1))
                .getAllItemsByUser(anyInt(), anyInt(), anyInt());
    }

    @Test
    void removeItem() {
        when(itemService.removeItem(anyInt())).thenReturn(item);

        try {
            mockMvc.perform(delete(endpoint + "/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(item.getName())))
                    .andExpect(jsonPath("$.description", is(item.getDescription())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1)).removeItem(anyInt());
    }

    @Test
    void searchItems() {
        when(itemService.searchItems(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(item));

        try {
            mockMvc.perform(get(endpoint + "/search")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("text", "test")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1))
                .searchItems(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void searchItemsWithThrow() {
        when(itemService.searchItems(anyInt(), anyString(), anyInt(), anyInt()))
                .thenThrow(UserNotFoundException.class);

        try {
            mockMvc.perform(get(endpoint + "/search")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 99)
                            .param("text", "test")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(itemService, times(1))
                .searchItems(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void searchItemsWithThrowTextNull() {
        try {
            mockMvc.perform(get(endpoint + "/search")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 1)
                            .param("from", "0")
                            .param("size", "1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}