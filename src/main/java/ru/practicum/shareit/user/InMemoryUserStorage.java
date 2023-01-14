package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.utils.GeneratorId;

import java.util.*;

@Repository
@Qualifier("inMemory")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private final Set<String> userEmails;
    private final GeneratorId generatorId;

    public InMemoryUserStorage(GeneratorId generatorId) {
        this.users = new HashMap<>();
        this.userEmails = new HashSet<>();
        this.generatorId = generatorId;
    }

    @Override
    public User add(User user) {
        User creatingUser = user.toBuilder().id(generatorId.getId()).build();
        users.put(creatingUser.getId(), creatingUser);
        userEmails.add(user.getEmail());
        return creatingUser;
    }

    @Override
    public User remove(User user) {
        userEmails.remove(user.getEmail());
        return users.remove(user.getId());
    }

    @Override
    public User remove(int userId) {
        if (users.containsKey(userId)) {
            userEmails.remove(users.get(userId).getEmail());
        }
        return users.remove(userId);
    }

    @Override
    public User update(User user) {
        var oldUser = users.get(user.getId());
        if (user.getName() != null && !user.getName().isEmpty()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userEmails.remove(users.get(user.getId()).getEmail());
            userEmails.add(user.getEmail());
            oldUser.setEmail(user.getEmail());
        }

        users.put(user.getId(), oldUser);
        return oldUser;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(int idUser) {
        return users.get(idUser);
    }

    @Override
    public boolean contains(int userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean contains(String email) {
        return userEmails.contains(email);
    }
}
