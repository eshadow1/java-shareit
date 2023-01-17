package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User updateUser(int id, UserDto user);

    User getUser(int userId);

    List<User> getAllUsers();

    User removeUser(int userId);
}
