package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private static User correctUser;

    private static User userWithoutEmail;

    private static User updateCorrectUser;

    @BeforeAll
    public static void beforeAll() {
        correctUser = User.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();
        userWithoutEmail = User.builder()
                .id(1)
                .name("user")
                .build();
        updateCorrectUser = User.builder()
                .id(1)
                .name("updateuser")
                .email("user@user.com")
                .build();
    }

    @Test
    void addUser() {
        var user = userService.addUser(correctUser);

        assertEquals(user.getId(), correctUser.getId());
        assertEquals(user.getName(), correctUser.getName());
        assertEquals(user.getEmail(), correctUser.getEmail());
    }

    @Test
    void addUserWithoutEmail() {
        assertThrows(
                ValidationException.class,
                () -> userService.addUser(userWithoutEmail));
    }


    @Test
    void updateNotFoundUser() {
        assertThrows(
                ContainsFalseException.class,
                () -> userService.updateUser(2, correctUser));
    }

    @Test
    void updateUser() {
        userService.addUser(correctUser);
        var updateUser = userService.updateUser(1, updateCorrectUser);

        assertEquals(updateUser.getId(), updateCorrectUser.getId());
        assertEquals(updateUser.getName(), updateCorrectUser.getName());
        assertEquals(updateUser.getEmail(), updateCorrectUser.getEmail());
    }

    @Test
    void getUser() {
        userService.addUser(correctUser);
        var user = userService.getUser(correctUser.getId());

        assertEquals(user.getId(), correctUser.getId());
        assertEquals(user.getName(), correctUser.getName());
        assertEquals(user.getEmail(), correctUser.getEmail());
    }

    @Test
    void getNotFoundUser() {
        assertThrows(
                ContainsFalseException.class,
                () -> userService.getUser(correctUser.getId()));
    }

    @Test
    void getAllUsers() {
        userService.addUser(correctUser);
        var users = userService.getAllUsers();

        assertEquals(users.size(), 1);
    }

    @Test
    void removeUser() {
        userService.addUser(correctUser);
        var user = userService.getUser(correctUser.getId());

        assertEquals(user.getId(), correctUser.getId());
        assertEquals(user.getName(), correctUser.getName());
        assertEquals(user.getEmail(), correctUser.getEmail());
    }
}