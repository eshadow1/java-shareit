package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private static final int USER_WITHOUT_ID = 0;
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос на добавление пользователя: " + user);

        return userService.addUser(UserMapper.fromUserDto(user, USER_WITHOUT_ID));
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody UserDto user) {
        log.info("Получен запрос на обновление пользователя " + id + ": " + user);

        return userService.updateUser(id, UserMapper.fromUserDto(user, id));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Получен запрос на получение пользователя " + id);

        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на получение всех пользователей");

        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public User removeUser(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя " + id);

        return userService.removeUser(id);
    }

}
