package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User get(int userId);

    User add(User user);

    User remove(User user);

    User remove(int userId);

    User update(User user);

    boolean contains(int userId);

    boolean contains(String email);
}
