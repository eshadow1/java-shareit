package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class UserControllerIntegralTest {

    private static String endpoint;

    private static String jsonCorrectUser;

    private static String jsonUpdateUser;

    private static UserDto correctUser;

    private static UserDto updateUser;

    private static String jsonUserWithoutEmail;

    private static String jsonUserWithIncorrectEmail;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeAll
    public static void beforeAll() {
        endpoint = "/users";

        correctUser = UserDto.builder().id(1).name("user").email("user@user.com").build();
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

        updateUser = UserDto.builder().id(1).name("updateuser").email("user@user.com").build();
        jsonUpdateUser = "{" +
                "    \"name\": \"updateuser\"," +
                "    \"email\": \"user@user.com\"" +
                "}";

    }

    @Test
    void addCorrectUser() {
        try {
            mockMvc.perform(post(endpoint)
                            .content(jsonCorrectUser)
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is(correctUser.getName())))
                    .andExpect(jsonPath("$.email", is(correctUser.getEmail())));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addCorrectUserAndGet() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());

            mockMvc.perform(get(endpoint + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(correctUser.getName())))
                    .andExpect(jsonPath("$.email", is(correctUser.getEmail())));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    }

    @Test
    void addUserWithUncorrectEmail() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonUserWithIncorrectEmail)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());

            mockMvc.perform(patch(endpoint + "/1")
                            .content(jsonUpdateUser)
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(updateUser.getName())));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateNotFoundUser() {
        try {
            mockMvc.perform(patch(endpoint + "/99")
                    .content(jsonUpdateUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserNotFound() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonCorrectUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());

            mockMvc.perform(get(endpoint + "/99")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUsers() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonUpdateUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());

            mockMvc.perform(get(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeUser() {
        try {
            mockMvc.perform(post(endpoint)
                    .content(jsonUpdateUser)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());

            mockMvc.perform(delete(endpoint + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}