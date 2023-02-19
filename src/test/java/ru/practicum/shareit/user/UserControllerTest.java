package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    private static String endpoint;

    private static String jsonCorrectUser;

    private static String jsonUpdateUser;

    private static UserDto correctUser;

    private static User user;

    private static User updateUser;

    private static String jsonUserWithoutEmail;

    private static String jsonUserWithIncorrectEmail;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {

        endpoint = "/users";

        correctUser = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();
        jsonCorrectUser = "{" +
                "    \"name\": \"user\"," +
                "    \"email\": \"user@user.com\"" +
                "}";

        jsonUserWithoutEmail = "{" +
                "    \"name\": \"user\"" +
                "}";

        jsonUserWithIncorrectEmail = "{" +
                "    \"name\": \"user\"," +
                "    \"email\": \"user.com\"" +
                "}";

        updateUser = User.builder()
                .id(1)
                .name("updateuser")
                .email("user@user.com")
                .build();
        jsonUpdateUser = "{" +
                "    \"name\": \"updateuser\"," +
                "    \"email\": \"user@user.com\"" +
                "}";
    }

    @Test
    void addUser() {
        when(userService.addUser(any())).thenReturn(user);

        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonCorrectUser)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is(correctUser.getName())))
                    .andExpect(jsonPath("$.email", is(correctUser.getEmail())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).addUser(any());
    }

    @Test
    void addUserWithoutEmail() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonUserWithoutEmail)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(0)).addUser(any());
    }

    @Test
    void addUserWithIncorrectEmail() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonUserWithIncorrectEmail)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(0)).addUser(any());
    }

    @Test
    void updateUser() {
        when(userService.updateUser(anyInt(), any())).thenReturn(updateUser);

        try {
            mockMvc.perform(patch(endpoint + "/1")
                            .content(jsonUpdateUser)
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(updateUser.getName())))
                    .andExpect(jsonPath("$.email", is(updateUser.getEmail())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).updateUser(anyInt(), any());
    }

    @Test
    void getUser() {
        when(userService.getUser(anyInt())).thenReturn(user);

        try {
            mockMvc.perform(get(endpoint + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(correctUser.getName())))
                    .andExpect(jsonPath("$.email", is(correctUser.getEmail())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).getUser(anyInt());
    }

    @Test
    void getUserNotFound() {
        when(userService.getUser(anyInt())).thenThrow(ContainsFalseException.class);

        try {
            mockMvc.perform(get(endpoint + "/99")
                            .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).getUser(anyInt());
    }

    @Test
    void getUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        try {
            mockMvc.perform(get(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void removeUser() {
        when(userService.removeUser(anyInt())).thenReturn(user);

        try {
            mockMvc.perform(delete(endpoint + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).removeUser(anyInt());
    }
}