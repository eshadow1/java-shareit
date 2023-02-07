package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
@Qualifier("inDb")
public class InDbUserStorage implements UserStorage {
    private final UserRepository userRepository;

    public InDbUserStorage(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public User remove(User user) {
        userRepository.delete(user);
        return user;
    }

    @Override
    public User remove(int userId) {
        var user = get(userId);
        userRepository.deleteById(userId);
        return user;
    }

    @Override
    public User update(User user) {
        var tempUser = userRepository.findById(user.getId()).orElseThrow();

        if (user.getEmail() != null) {
            tempUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            tempUser.setName(user.getName());
        }

        return userRepository.save(tempUser);
    }

    @Override
    public boolean contains(int userId) {
        return userRepository.findById(userId).isPresent();
    }

    @Override
    public boolean contains(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
