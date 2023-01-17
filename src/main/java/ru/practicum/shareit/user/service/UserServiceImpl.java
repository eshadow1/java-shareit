package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.ContainsTrueException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(@Qualifier("inMemory") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        checkedUserContainsByEmail(user.getEmail());

        return userStorage.add(user);
    }

    public User updateUser(int id, UserDto user) {
        checkedUserContains(id);
        if (user.getEmail() != null
                && !user.getEmail().isEmpty()
                && checkedUpdatedEmail(id, user.getEmail())) {
            checkedUserContainsByEmail(user.getEmail());
        }

        return userStorage.update(UserMapper.fromUserDto(user, id));
    }

    public User getUser(int userId) {
        checkedUserContains(userId);

        return userStorage.get(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User removeUser(int userId) {
        return userStorage.remove(userId);
    }

    private void checkedUserContains(int id) {
        if (!userStorage.contains(id)) {
            throw new ContainsFalseException("Пользователь с id " + id + " не найден");
        }
    }

    private void checkedUserContainsByEmail(String email) {
        if (userStorage.contains(email)) {
            throw new ContainsTrueException("Пользователь с почтой " + email + " уже существует");
        }
    }

    private boolean checkedUpdatedEmail(int id, String email) {
        return !userStorage.get(id).getEmail().equals(email);
    }
}
