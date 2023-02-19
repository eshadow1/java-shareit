package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.exception.ContainsFalseException;
import ru.practicum.shareit.utils.exception.ContainsTrueException;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(int id, User user) {
        checkedUserContains(id);
        if (user.getEmail() != null
                && !user.getEmail().isEmpty()
                && checkedUpdatedEmail(id, user.getEmail())) {
            checkedUserContainsByEmail(user.getEmail());
        }

        var tempUser = userRepository.findById(user.getId()).orElseThrow();

        if (user.getEmail() != null) {
            tempUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            tempUser.setName(user.getName());
        }

        return userRepository.save(tempUser);
    }

    public User getUser(int userId) {
        checkedUserContains(userId);

        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User removeUser(int userId) {
        var user = userRepository.findById(userId).orElse(null);
        userRepository.deleteById(userId);
        return user;
    }

    private void checkedUserContains(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ContainsFalseException("Пользователь с id " + id + " не найден");
        }
    }

    private void checkedUserContainsByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ContainsTrueException("Пользователь с почтой " + email + " уже существует");
        }
    }

    private boolean checkedUpdatedEmail(int id, String email) {
        return !userRepository.findById(id).get().getEmail().equals(email);
    }
}
