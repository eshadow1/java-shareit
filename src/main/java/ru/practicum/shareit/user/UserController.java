package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: " + user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя " + id + ": " + user);


        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        log.info("Получен запрос на получение пользователя " + id);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя " + id);

        return ResponseEntity.status(HttpStatus.OK).body(userService.removeUser(id));
    }

}
